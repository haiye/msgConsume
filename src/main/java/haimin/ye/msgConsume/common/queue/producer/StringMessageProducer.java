package haimin.ye.msgConsume.common.queue.producer;

import haimin.ye.msgConsume.common.queue.message.Message;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class StringMessageProducer implements MessageProducer {
    Logger logger = Logger.getLogger(StringMessageProducer.class);

    private BlockingQueue<Message> messageQueue;
    private Message message;

    public StringMessageProducer(BlockingQueue<Message> messageQueue, Message message) {
        logger.trace(Thread.currentThread().getName() + " is init...");
        this.messageQueue = messageQueue;
        this.message = message;
    }

    public void run() {
        logger.trace(Thread.currentThread().getName() + " is running...");
        enqueue(messageQueue, message);
        logger.trace(Thread.currentThread().getName() + " is releasing...");

    }

    /**
     * put one piece of message into messageQueue
     * 
     * @param messageQueue
     * @param message
     */
    public void enqueue(BlockingQueue<Message> messageQueue, Message message) {
        try {
            logger.trace(Thread.currentThread().getName() + " enqueue begin; messageQueue.size: " + messageQueue.size()
                    + "message: " + message);
            messageQueue.put(message);
            Thread.sleep(500);
            logger.debug("message content to be enqueued: "+message);
            logger.trace(Thread.currentThread().getName() + " enqueue end; messageQueue.size: " + messageQueue.size()
                    + "message: " + message);

        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("error happened while enqueue; current thread: " + Thread.currentThread().getName()
                    + "; exception: " + e);
        }
    }

}
