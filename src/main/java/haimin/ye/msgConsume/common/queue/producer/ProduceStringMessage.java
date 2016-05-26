package haimin.ye.msgConsume.common.queue.producer;

import haimin.ye.msgConsume.common.queue.message.MessageInterface;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class ProduceStringMessage implements ProduceMessageInterface {
    private static Logger logger = Logger.getLogger("ConsumeMessage");

    private BlockingQueue<MessageInterface> messageQueue;
    private MessageInterface message;
    private int tag;
    public ProduceStringMessage(BlockingQueue<MessageInterface> messageQueue, MessageInterface message) {
        this.messageQueue = messageQueue;
        this.message = message;
        tag=0;
    }
    public ProduceStringMessage(BlockingQueue<MessageInterface> messageQueue, MessageInterface message,int tag) {
        this.messageQueue = messageQueue;
        this.message = message;
        this.tag=tag;
    }
    public void run() {
        enqueue(messageQueue, message);
    }

    public void enqueue(BlockingQueue<MessageInterface> messageQueue, MessageInterface message) {
        try {
            System.out.println("producer thread: "+Thread.currentThread().getName()+"_"+tag+"; enqueue: begin; message: "+message+"; messageQueue.size: "+messageQueue.size());
            messageQueue.put(message);
            System.out.println("producer thread: "+Thread.currentThread().getName()+"_"+tag+"; enqueue: end; message: "+message+"; messageQueue.size: "+messageQueue.size());

        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.info("error happened while enqueue; current thread: "+Thread.currentThread().getName()+"; exception: "+e);
        }
    }

}
