package haimin.ye.msgConsume.common.queue.producer;

import haimin.ye.msgConsume.common.queue.message.Message;

import java.util.concurrent.BlockingQueue;

public interface MessageProducer extends Runnable {

    public void run();

    /**
     * put one piece of message into messageQueue
     * 
     * @param messageQueue
     * @param message
     */
    void enqueue(BlockingQueue<Message> messageQueue, Message message);

}
