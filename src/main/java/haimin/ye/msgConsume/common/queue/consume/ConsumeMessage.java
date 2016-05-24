package haimin.ye.msgConsume.common.queue.consume;

import haimin.ye.msgConsume.common.Message;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public interface ConsumeMessage extends Runnable {

    static Logger logger = Logger.getLogger("ConsumeMessage");

    BlockingQueue<Message> messageQueue = null;

    public void run();

}
