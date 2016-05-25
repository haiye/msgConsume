package haimin.ye.msgConsume.common.queue.consumer;

import haimin.ye.msgConsume.common.Message;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class ConsumeMessageMutiThreads {

    private static Logger logger = Logger.getLogger("ConsumeMessage");

    private BlockingQueue<Message> messageQueue;
    @SuppressWarnings("rawtypes")
    private Class consumeClient;
    private int numThreads;

    @SuppressWarnings("rawtypes")
    public ConsumeMessageMutiThreads(BlockingQueue<Message> messageQueue, Class consumeClient, int numThreads) {
        this.messageQueue = messageQueue;
        this.consumeClient = consumeClient;
        this.numThreads = numThreads;
    }

    @SuppressWarnings("unchecked")
    public void consumeQueue() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < numThreads; ++i) {
            Runnable c = null;
            if (consumeClient.equals(StrToFile.class)) {
                // c = new StrConsumeToFile(messageQueue );
                c = (Runnable) consumeClient.getConstructor(BlockingQueue.class).newInstance(messageQueue);

            }
            Thread t = new Thread(null, c, consumeClient.getSimpleName() + "-" + i + "-Thread");
            System.out.println("new thread="+t.toString());
            
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                public void uncaughtException(Thread t, Throwable e) {
                    logger.info("Client Error in " + t.getName() + "; " + e);
                }
            });
            threads.add(t);
            t.start();
        }

    }

}
