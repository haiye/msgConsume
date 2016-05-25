package haimin.ye.msgConsume.common.queue.producer;

import haimin.ye.msgConsume.common.Message;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class StrToQueue implements ProduceMessageInterface {
    private static Logger logger = Logger.getLogger("ConsumeMessage");

    private BlockingQueue<Message> messageQueue;
    private Message message;

    public StrToQueue(BlockingQueue<Message> messageQueue, Message message) {
        this.messageQueue = messageQueue;
        this.message = message;
    }

    public void run() {
        enqueue(messageQueue, message);
    }

    public void enqueue(BlockingQueue<Message> messageQueue, Message message) {
        try {
            System.out.println("current thread: "+Thread.currentThread().getName()+"; enqueue: begin; message: "+message);
            messageQueue.put(message);
            System.out.println("current thread: "+Thread.currentThread().getName()+"enqueue: end; message: "+message);

        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.info("error happened while enqueue; current thread: "+Thread.currentThread().getName()+"; exception: "+e);
        }
    }

}
