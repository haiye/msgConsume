package haimin.ye.msgConsume.common.queue.producer;

import haimin.ye.msgConsume.common.Message;
import java.util.concurrent.BlockingQueue;

public interface ProduceMessageInterface extends Runnable {

    public void run();

    void enqueue(BlockingQueue<Message> messageQueue, Message message);

}
