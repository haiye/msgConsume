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

//import java.util.logging.Logger;

public class MessageProducerMultyThreads {

    // private static Logger logger = Logger.getLogger("MsgQueue");

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

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        BufferedReader br = null;

        String sCurrentLine;
        int thread_num = 1;
        int count = 1;
        try {
            br = getDriverReader(driverFile);
            while ((sCurrentLine = br.readLine()) != null) {

                Runnable c = null;
                Message message = new StringMessage("file_name" + (count++), sCurrentLine);
                if (producerClient.equals(StringMessageProducer.class)) {
                    c = (Runnable) producerClient.getConstructor(BlockingQueue.class, Message.class, int.class)
                            .newInstance(messageQueue, message, thread_num);
                }
                System.out.println("new producer thread=" + (thread_num++));

                executor.execute(c);

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

        int i = 0;
        while (!executor.isTerminated()) {

            System.out.println("waiting all threads released" + (i++));

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All threads are released");

        System.out.println("add end_tag into queue2");
        try {
            messageQueue.put(Constant.END_TAG);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 
     * put message into messageQueue
     * 
     * 
     * numThreads of producer would running at most, and start a new thread to produce message once previous thread is finished
     * 
     * Good: only numThreads of producer started at the same time, which cause less resources
     * */

    @SuppressWarnings("unchecked")
    public void produceQueue2() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        List<Thread> threads = new ArrayList<Thread>();
        int thread_num = 1;
        BufferedReader br = null;

        String sCurrentLine;
        int count = 1;

        try {
            br = getDriverReader(this.driverFile);

            System.out.println("target_thread_num" + numThreads + "; threads.size0()=" + threads.size());
            while (((sCurrentLine = br.readLine()) != null) && threads.size() <= numThreads) {

                // waiting thread release if we already enough threads
                int time_to_wait_release = 0;
                while (threads.size() == numThreads) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println("target_thread_num" + numThreads + "; threads.size1()=" + threads.size()
                            + ";time_to_wait_release =" + time_to_wait_release);

                    for (int i = 0; i < threads.size(); i++) {
                        Thread thread = threads.get(i);
                        System.out.println("thread.name" + thread.getName() + "; thread status=" + thread.getState());

                        if (thread.getState() == State.TERMINATED) {
                            System.out.println("target_thread_num" + numThreads + "; threads.size2()=" + threads.size()
                                    + ";time_to_wait_release =" + time_to_wait_release);

                            threads.remove(thread);
                            System.out.println("target_thread_num" + numThreads + "; threads.size3()=" + threads.size()
                                    + ";time_to_wait_release =" + time_to_wait_release);

                        }
                    }
                    System.out.println("target_thread_num" + numThreads + "; threads.size4()=" + threads.size()
                            + ";time_to_wait_release =" + time_to_wait_release);
                    time_to_wait_release++;
                }

                Message message = new StringMessage("file_name" + (count++), sCurrentLine);

                Runnable c = null;
                if (producerClient.equals(StringMessageProducer.class)) {
                    c = (Runnable) producerClient.getConstructor(BlockingQueue.class, Message.class, int.class)
                            .newInstance(messageQueue, message, (thread_num++));
                }

                Thread t = new Thread(null, c, producerClient.getSimpleName() + "-" + thread_num + "-Thread");
                t.getState();
                Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                    public void uncaughtException(Thread t, Throwable e) {
                        System.out.println("Client Error in " + t.getName() + "; " + e);
                        t.interrupt();
                    }
                });
                threads.add(t);
                t.start();
            }

            try {
                messageQueue.put(Constant.END_TAG);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    
    public void produceQueue3(){
        
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
