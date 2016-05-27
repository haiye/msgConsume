package haimin.ye.msgConsume.common.queue.producer;

import haimin.ye.msgConsume.common.Constant;
import haimin.ye.msgConsume.common.queue.message.Message;
import haimin.ye.msgConsume.common.queue.message.StringMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Thread.State;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

//import java.util.logging.Logger;

public class MessageProducerMultyThreads {

    Logger logger = Logger.getLogger(MessageProducerMultyThreads.class);

    private BlockingQueue<Message> messageQueue;
    @SuppressWarnings("rawtypes")
    private Class producerClient;
    private int numThreads;
    private String driverFile;

    @SuppressWarnings("rawtypes")
    public MessageProducerMultyThreads(String driverFile, BlockingQueue<Message> messageQueue, Class consumeClient,
            int numThreads) {
        this.driverFile = driverFile;
        this.messageQueue = messageQueue;
        this.producerClient = consumeClient;
        this.numThreads = numThreads;
    }

    @SuppressWarnings("rawtypes")
    public MessageProducerMultyThreads(String driverFile, BlockingQueue<Message> messageQueue, Class consumeClient) {
        this.driverFile = driverFile;
        this.messageQueue = messageQueue;
        this.producerClient = consumeClient;
        this.numThreads = Constant.DEFAULT_NUM_THREADS_PRODUCER;
    }

    /**
     * 
     * put message into messageQueue
     * 
     * start many threads and only numThreads of producer would running, others
     * are waiting in this case, threads number is same as number of lines in
     * driverFile
     * 
     * Bad: too many threads are started which cause lots of CPU and memory
     * resources
     * */
    @SuppressWarnings("unchecked")
    public void produceQueue() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        logger.trace(Thread.currentThread().getName() + " produceQueue is beginning...");

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        BufferedReader br = null;

        String sCurrentLine;
        int thread_num = 1;
        int count = 1;
        try {
            br = getDriverReader(driverFile);
            while ((sCurrentLine = br.readLine()) != null) {

                Runnable runnableInstance = null;
                Message message = new StringMessage("file_name" + (count++), sCurrentLine);
                if (producerClient.equals(StringMessageProducer.class)) {
                    runnableInstance = (Runnable) producerClient.getConstructor(BlockingQueue.class, Message.class).newInstance(
                            messageQueue, message);
                    
                }
                String threadName = producerClient.getSimpleName() + "_thread_" + (thread_num++);

                Thread aThread = new Thread(null, runnableInstance, threadName);
                executor.execute(aThread);
            }
            executor.shutdown();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

        while (!executor.isTerminated()) {
            logger.trace(Thread.currentThread().getName() + "waiting all producer threads released...");

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.trace("All threads are released");

        try {
            messageQueue.put(Constant.END_TAG);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.trace(Thread.currentThread().getName() + " produceQueue is ending...");

    }

    /**
     * 
     * put message into messageQueue
     * 
     * 
     * numThreads of producer would running at most, and start a new thread to
     * produce message once previous thread is finished
     * 
     * Good: only numThreads of producer started at the same time, which cause
     * less resources
     * */

    @SuppressWarnings("unchecked")
    public void produceQueue2() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        logger.debug(Thread.currentThread().getName() + " produceQueue is beginning...");

        List<Thread> threads = new ArrayList<Thread>();
        int thread_num = 1;
        BufferedReader br = null;

        String sCurrentLine;
        int count = 1;

        try {
            br = getDriverReader(this.driverFile);

            while (((sCurrentLine = br.readLine()) != null) && threads.size() <= numThreads) {

                // waiting thread release if we already enough threads
                while (threads.size() == numThreads) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    for (int i = 0; i < threads.size(); i++) {
                        Thread thread = threads.get(i);
                        if (thread.getState() == State.TERMINATED) {
                            threads.remove(thread);

                        }
                    }

                }

                Message message = new StringMessage("file_name" + (count++), sCurrentLine);

                Runnable runnableInstance = null;
                if (producerClient.equals(StringMessageProducer.class)) {
                    runnableInstance = (Runnable) producerClient.getConstructor(BlockingQueue.class, Message.class).newInstance(
                            messageQueue, message);
                }

                String threadName = producerClient.getSimpleName() + "_thread_" + (thread_num++);

                Thread aThread = new Thread(null, runnableInstance, threadName);
                aThread.getState();
                Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                    public void uncaughtException(Thread t, Throwable e) {
                        logger.error("Client Error in " + t.getName() + "; " + e);
                        t.interrupt();
                    }
                });
                threads.add(aThread);
                aThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
        try {
            messageQueue.put(Constant.END_TAG);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug(Thread.currentThread().getName() + " produceQueue is ending...");

    }

    /**
     * 
     * put message into messageQueue
     * 
     * 
     * numThreads of producer would running at most
     * compare to produceQueue2(), it's easier to maintains
     * 
     * Good: only numThreads of producer started at the same time, which cause less resources
     * 
     * NOTE
     * ThreadPoolExecutor.CallerRunsPolicy():
     * while blockingQueue.size and maximumPoolSize are full, re-send thread into thread pool
     * 
     * ThreadPoolExecutor.DiscardOldestPolicy()
     * while blockingQueue.size and maximumPoolSize are full, discard oldest thread and run threadPool.execute() again to add new thread
     * 
     * ThreadPoolExecutor.DiscardPolicy()
     * while blockingQueue.size and maximumPoolSize are full, discard all new threads who are trying to add into blocking queue
     * */

    @SuppressWarnings("unchecked")
    public void produceQueue3() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        logger.debug(Thread.currentThread().getName() + " produceQueue is beginning...");

        BlockingQueue<Runnable> producerBlockingQueue = new LinkedBlockingQueue<Runnable>(
                Constant.DEFAULT_NUM_THREADS_PRODUCER_BlOCKINGQUEUESIZE);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(numThreads, numThreads * 2, 100, TimeUnit.MILLISECONDS,
                producerBlockingQueue, new ThreadPoolExecutor.CallerRunsPolicy());

        List<Thread> threads = new ArrayList<Thread>();
        int thread_num = 1;
        BufferedReader br = null;

        String sCurrentLine;
        int count = 1;

        try {
            br = getDriverReader(this.driverFile);

            while (((sCurrentLine = br.readLine()) != null) && threads.size() <= numThreads) {

                Message message = new StringMessage("file_name" + (count++), sCurrentLine);

                Runnable runnableInstance = null;
                if (producerClient.equals(StringMessageProducer.class)) {
                    runnableInstance = (Runnable) producerClient.getConstructor(BlockingQueue.class, Message.class).newInstance(
                            messageQueue, message);
                }
                String threadName = producerClient.getSimpleName() + "_thread_" + (thread_num++);

                Thread aThread = new Thread(null, runnableInstance, threadName);
                threadPool.execute(aThread);

            }

            threadPool.shutdown();

            while (!threadPool.isTerminated()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.trace(Thread.currentThread().getName() + "waiting all producer threads released...");
            }
            logger.trace("All threads are released");

        } catch (IOException e) {
            e.printStackTrace();

        }

        try {
            messageQueue.put(Constant.END_TAG);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.trace(Thread.currentThread().getName() + " produceQueue is ending...");

    }

    private BufferedReader getDriverReader(String driverFile) throws FileNotFoundException {
        BufferedReader br;
        StringBuilder sbfp = getFilePath(driverFile);
        File f = new File(sbfp.toString());
        if (f.exists() && !f.isDirectory()) {
            br = new BufferedReader(new FileReader(sbfp.toString()));
        } else {
            br = new BufferedReader(new FileReader(sbfp.insert(0, "src/main/resources/").toString()));
        }
        return br;
    }

    private StringBuilder getFilePath(String driverFilePath) {
        StringBuilder sbfp = new StringBuilder();
        if (!driverFilePath.startsWith("/")) {
            sbfp.append("./");
        }
        sbfp.append(driverFilePath);
        return sbfp;
    }

}
