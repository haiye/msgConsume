package haimin.ye.paypal.msgConsume;

import haimin.ye.msgConsume.common.queue.MessageBlockingQueue;
import haimin.ye.msgConsume.common.queue.consumer.ConsumeMessageMutiThreads;
import haimin.ye.msgConsume.common.queue.consumer.ConsumeStringMessage;
import haimin.ye.msgConsume.common.queue.message.MessageInterface;
import haimin.ye.msgConsume.common.queue.producer.PublishMessageMultiThreads;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;

public class MessgeConsumeTest {

    public static void main(String args[]) {

        // Logger logger = Logger.getLogger("MsgQueu22222e");
        // logger.info("yehaimin");
        // logger.warning("hm");
        //
        PublishMessageMultiThreads publishMessageTest = new PublishMessageMultiThreads();

        BlockingQueue<MessageInterface> queueTest = new MessageBlockingQueue<MessageInterface>(40);
        ConsumeMessageMutiThreads aConsumeMessageMutiThreads = new ConsumeMessageMutiThreads(queueTest, ConsumeStringMessage.class, 15);
        try {
            aConsumeMessageMutiThreads.consumeQueue();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        publishMessageTest.publishFromFile(queueTest, "src/test/java/haimin/ye/paypal/msgConsume/input_publish.txt");

    }

}
