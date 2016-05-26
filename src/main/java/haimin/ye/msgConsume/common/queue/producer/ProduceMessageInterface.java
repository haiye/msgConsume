package haimin.ye.msgConsume.common.queue.producer;

import haimin.ye.msgConsume.common.queue.message.MessageInterface;

import java.util.concurrent.BlockingQueue;

public interface ProduceMessageInterface extends Runnable {

    public void run();

    void enqueue(BlockingQueue<MessageInterface> messageQueue, MessageInterface message);

}
