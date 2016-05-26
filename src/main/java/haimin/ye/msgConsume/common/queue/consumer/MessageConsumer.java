package haimin.ye.msgConsume.common.queue.consumer;

import haimin.ye.msgConsume.common.queue.message.Message;

import java.util.concurrent.BlockingQueue;

public interface MessageConsumer extends Runnable {

    public void run();

    /**
     * consume message from messageQueue
     * 
     * @param messageQueue
     */
    public void dequeue(BlockingQueue<Message> messageQueue);

}
