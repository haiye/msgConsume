package haimin.ye.msgConsume.common.queue.consumer;

import haimin.ye.msgConsume.common.queue.message.Message;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public interface ConsumeMessageInterface extends Runnable {

    static Logger logger = Logger.getLogger("ConsumeMessage");

    BlockingQueue<Message> messageQueue = null;

    public void run();
    
    public void dequeue(BlockingQueue<Message> messageQueue);

}
