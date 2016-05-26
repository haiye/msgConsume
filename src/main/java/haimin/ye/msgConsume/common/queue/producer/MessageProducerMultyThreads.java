package haimin.ye.msgConsume.common.queue.producer;

import haimin.ye.msgConsume.common.Constant;
import haimin.ye.msgConsume.common.queue.message.Message;
import haimin.ye.msgConsume.common.queue.message.StringMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
