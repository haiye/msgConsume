package haimin.ye.msgConsume.common.queue.producer;

import haimin.ye.msgConsume.common.queue.message.Message;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public interface MessageProducer extends Runnable {
    Logger logger = Logger.getLogger(MessageProducer.class);

    public void run();

    /**
     * put one piece of message into messageQueue
     * 
     * @param messageQueue
     * @param message
     */
    void enqueue(BlockingQueue<Message> messageQueue, Message message);

}
