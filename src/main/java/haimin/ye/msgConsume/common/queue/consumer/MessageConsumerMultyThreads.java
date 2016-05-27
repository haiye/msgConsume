package haimin.ye.msgConsume.common.queue.consumer;

import haimin.ye.msgConsume.common.queue.message.Message;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class MessageConsumerMultyThreads {

    Logger logger = Logger.getLogger(MessageConsumerMultyThreads.class);

    private BlockingQueue<Message> messageQueue;
    @SuppressWarnings("rawtypes")
    private Class consumerClient;
    private int threadsNum;

    @SuppressWarnings("rawtypes")
    public MessageConsumerMultyThreads(BlockingQueue<Message> messageQueue, Class consumeClient, int threadsNum) {
        logger.trace(Thread.currentThread().getName() + " is init...");
        this.messageQueue = messageQueue;
        this.consumerClient = consumeClient;
        this.threadsNum = threadsNum;
    }

    /**
     * 
     * consume message from messageQueue
     * 
     * start numThreads of threads, and keep all of them active and take message
     * from queue one by one NOTE: only numThreads of threads would be started
     * */
    @SuppressWarnings("unchecked")
    public void consumeMessageQueue() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        logger.trace(Thread.currentThread().getName() + " consumeMessageQueue is beginning...");

        List<Thread> threadsList = new ArrayList<Thread>();
        for (int i = 0; i < threadsNum; ++i) {
            Runnable runnableInstance = null;
            if (consumerClient.equals(StringMessageConsumer.class)) {
                // c = new StrConsumeToFile(messageQueue );
                runnableInstance = (Runnable) consumerClient.getConstructor(BlockingQueue.class).newInstance(
                        messageQueue);
            }
            String threadName = consumerClient.getSimpleName() + "_thread_" + i;
            Thread aThread = new Thread(null, runnableInstance, threadName);

            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                public void uncaughtException(Thread thread, Throwable e) {
                    logger.error("Client Error in " + thread.getName() + "; " + e);
                }
            });
            threadsList.add(aThread);
            aThread.start();
        }
        logger.trace(Thread.currentThread().getName() + " consumeMessageQueue is ending...");

    }

}
