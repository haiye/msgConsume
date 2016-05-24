package haimin.ye.msgConsume.common.queue.consume;

import haimin.ye.msgConsume.common.Constant;
import haimin.ye.msgConsume.common.Message;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class StrConsumeToFile implements ConsumeMessage {

    private static Logger logger = Logger.getLogger("ConsumeMessage");

    private BlockingQueue<Message> messageQueue;

    public StrConsumeToFile(BlockingQueue<Message> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void run() {
        logger.info("Running " + this.getClass().getName());
        Message message = null;
        try {
            while ((message = messageQueue.take()) != Constant.END_TAG) {
                String message_str = message.getaMessage();
                // logger.info(message_str);
                System.out.println("this is a thread:" + message_str);

            }
            messageQueue.put(Constant.END_TAG);
            logger.info("Consumer Thread is released");

        } catch (Exception e) {
            logger.info(e + "");
            e.printStackTrace();
        }
    }

}
