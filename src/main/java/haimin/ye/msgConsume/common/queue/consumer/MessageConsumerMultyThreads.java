package haimin.ye.msgConsume.common.queue.consumer;

import haimin.ye.msgConsume.common.queue.message.Message;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class MessageConsumerMultyThreads {

    private static Logger logger = Logger.getLogger("ConsumeMessage");

    private BlockingQueue<Message> messageQueue;
    @SuppressWarnings("rawtypes")
    private Class consumerClient;
    private int numThreads;

    @SuppressWarnings("rawtypes")
    public MessageConsumerMultyThreads(BlockingQueue<Message> messageQueue, Class consumeClient, int numThreads) {
        this.messageQueue = messageQueue;
        this.consumerClient = consumeClient;
        this.numThreads = numThreads;
    }

    /**
     * 
     * consume message from messageQueue
     * 
     * start numThreads of threads, and keep all of them active and take message from queue one by one
     * NOTE: only numThreads of threads would be started
     * */
    @SuppressWarnings("unchecked")
    public void consumeQueue() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {

        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < numThreads; ++i) {
            Runnable c = null;
            if (consumerClient.equals(StringMessageConsumer.class)) {
                // c = new StrConsumeToFile(messageQueue );
                c = (Runnable) consumerClient.getConstructor(BlockingQueue.class).newInstance(messageQueue);

            }
            Thread t = new Thread(null, c, consumerClient.getSimpleName() + "-" + i + "-Thread");
            System.out.println("new consumer thread=" + t.toString());

            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                public void uncaughtException(Thread t, Throwable e) {
                    logger.info("Client Error in " + t.getName() + "; " + e);
                }
            });
            threads.add(t);
            t.start();
            System.out.println("debug: threads size="+threads.size());
        }
        System.out.println("debug2: threads size="+threads.size());

    }

}
