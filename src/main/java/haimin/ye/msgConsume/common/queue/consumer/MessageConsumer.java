package haimin.ye.msgConsume.common.queue.consumer;

import haimin.ye.msgConsume.common.queue.message.Message;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public interface MessageConsumer extends Runnable {
    Logger logger = Logger.getLogger(MessageConsumer.class);

    public void run();

    /**
     * consume message from messageQueue
     * 
     * @param messageQueue
     */
    public void dequeue(BlockingQueue<Message> messageQueue);

}
