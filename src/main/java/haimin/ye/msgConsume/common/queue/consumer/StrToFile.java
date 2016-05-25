package haimin.ye.msgConsume.common.queue.consumer;

import haimin.ye.msgConsume.common.Constant;
import haimin.ye.msgConsume.common.Message;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class StrToFile implements ConsumeMessageInterface {

    private static Logger logger = Logger.getLogger("ConsumeMessage");

    private BlockingQueue<Message> messageQueue;

    public StrToFile(BlockingQueue<Message> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void run() {
        dequeue(messageQueue);
    }

    public void dequeue(BlockingQueue<Message> messageQueue) {
        Message message = null;
        try {
            while ((message = messageQueue.take()) != Constant.END_TAG) {
                String message_str = message.getMessage();
                // logger.info(message_str);
                System.out.println("current thread: "+Thread.currentThread().getName()+"; current message:" + message_str);
            }
            messageQueue.put(Constant.END_TAG);
            logger.info("Consumer Thread is released");

        } catch (Exception e) {
            logger.info(e + "");
            e.printStackTrace();
        }
    }

}
