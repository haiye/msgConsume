package haimin.ye.msgConsume.common.queue.consumer;

import haimin.ye.msgConsume.common.Constant;
import haimin.ye.msgConsume.common.queue.message.Message;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class StringMessageConsumer implements MessageConsumer {
    Logger logger = Logger.getLogger(StringMessageConsumer.class);

    private BlockingQueue<Message> messageQueue;

    public StringMessageConsumer(BlockingQueue<Message> messageQueue) {
        logger.trace(Thread.currentThread().getName() + " is init...");
        this.messageQueue = messageQueue;
    }

    public void run() {
        logger.trace(Thread.currentThread().getName() + " is running...");
        dequeue(messageQueue);
        logger.trace(Thread.currentThread().getName() + " is releasing...");

    }

    /**
     * consume message from messageQueue
     * 
     * @param messageQueue
     */
    public void dequeue(BlockingQueue<Message> messageQueue) {
        Message message = null;
        try {
            while ((message = messageQueue.take()) != Constant.END_TAG) {
                logger.trace(Thread.currentThread().getName() + " deqeue begin; messageQueue.size: "
                        + messageQueue.size());
                String message_str = message.getMessage();
                /*
                 * add logic to deal with message_str like write string to files
                 */
                Thread.sleep(100);
                logger.debug("message content to be dequeued: "+message);

                logger.trace(Thread.currentThread().getName() + " deqeue end; messageQueue.size: "
                        + messageQueue.size() + "; message_str:" + message_str);
            }
            messageQueue.put(Constant.END_TAG);

        } catch (Exception e) {
            logger.error("error happened while dequeue; current thread: " + Thread.currentThread().getName()
                    + "; exception: " + e);
            e.printStackTrace();
        }
    }

}
