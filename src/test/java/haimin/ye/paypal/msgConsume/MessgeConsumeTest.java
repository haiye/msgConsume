package haimin.ye.paypal.msgConsume;

import haimin.ye.msgConsume.common.Message;
import haimin.ye.msgConsume.common.queue.TrackingBlockingQueue;
import haimin.ye.msgConsume.common.queue.consume.ConsumeMessageMutiThreads;
import haimin.ye.msgConsume.common.queue.consume.StrConsumeToFile;
import haimin.ye.msgConsume.common.queue.publish.PublishMessage;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;

import org.junit.BeforeClass;
import org.junit.Test;

public class MessgeConsumeTest {

    public static void main(String args[]) {

        // Logger logger = Logger.getLogger("MsgQueu22222e");
        // logger.info("yehaimin");
        // logger.warning("hm");
        //
        PublishMessage publishMessageTest = new PublishMessage();

        BlockingQueue<Message> queueTest = new TrackingBlockingQueue<Message>(40);
        ConsumeMessageMutiThreads aConsumeMessageMutiThreads = new ConsumeMessageMutiThreads(queueTest,
                StrConsumeToFile.class, 15);
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
