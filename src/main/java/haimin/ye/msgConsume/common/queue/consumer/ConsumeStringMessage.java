package haimin.ye.msgConsume.common.queue.consumer;

import haimin.ye.msgConsume.common.Constant;
import haimin.ye.msgConsume.common.queue.message.Message;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class ConsumeStringMessage implements ConsumeMessageInterface {

    private static Logger logger = Logger.getLogger("ConsumeMessage");

    private BlockingQueue<Message> messageQueue;

    public ConsumeStringMessage(BlockingQueue<Message> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void run() {
        dequeue(messageQueue);
    }

    public void dequeue(BlockingQueue<Message> messageQueue) {
        Message message = null;
        try {
            while ((message = messageQueue.take()) != Constant.END_TAG) {
                System.out.println("consumer thread: "+Thread.currentThread().getName()+"; deqeue: begin; messageQueue.size: "+messageQueue.size());

                String message_str = message.getMessage();
                /*
                 * add logic to deal with message_str
                 * */
                Thread.sleep(2000);
                
                System.out.println("consumer thread: "+Thread.currentThread().getName()+"; deqeue: end; messageQueue.size: "+messageQueue.size()+"; message_str:"+message_str);
            }
            messageQueue.put(Constant.END_TAG);
//            logger.info("Consumer Thread is released");

        } catch (Exception e) {
            logger.info(e + "");
            e.printStackTrace();
        }
    }

}
