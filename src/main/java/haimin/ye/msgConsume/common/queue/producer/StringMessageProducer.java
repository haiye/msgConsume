package haimin.ye.msgConsume.common.queue.producer;

import haimin.ye.msgConsume.common.queue.message.Message;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class StringMessageProducer implements MessageProducer {
    private static Logger logger = Logger.getLogger("ConsumeMessage");

    private BlockingQueue<Message> messageQueue;
    private Message message;
    private int tag;

    public StringMessageProducer(BlockingQueue<Message> messageQueue, Message message) {
        this.messageQueue = messageQueue;
        this.message = message;
        tag = 0;
    }

    public StringMessageProducer(BlockingQueue<Message> messageQueue, Message message, int tag) {
        System.out.println("producer thread: " + Thread.currentThread().getName() + "_" + tag+" is ready");
        this.messageQueue = messageQueue;
        this.message = message;
        this.tag = tag;
    }

    public void run() {
        enqueue(messageQueue, message);
    }

    /**
     * put one piece of message into messageQueue
     * 
     * @param messageQueue
     * @param message
     */
    public void enqueue(BlockingQueue<Message> messageQueue, Message message) {
        try {
            System.out.println("producer thread: " + Thread.currentThread().getName() + "_" + tag
                    + "; enqueue: begin; message: " + message + "; messageQueue.size: " + messageQueue.size());
            messageQueue.put(message);
            Thread.sleep(500);
            System.out.println("producer thread: " + Thread.currentThread().getName() + "_" + tag
                    + "; enqueue: end; message: " + message + "; messageQueue.size: " + messageQueue.size());

        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.info("error happened while enqueue; current thread: " + Thread.currentThread().getName()
                    + "; exception: " + e);
        }
    }

}
