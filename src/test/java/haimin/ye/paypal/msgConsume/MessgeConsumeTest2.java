package haimin.ye.paypal.msgConsume;

import haimin.ye.msgConsume.common.queue.MessageBlockingQueue;
import haimin.ye.msgConsume.common.queue.consumer.ConsumeMessageMutiThreads;
import haimin.ye.msgConsume.common.queue.consumer.ConsumeStringMessage;
import haimin.ye.msgConsume.common.queue.message.MessageInterface;
import haimin.ye.msgConsume.common.queue.producer.ProduceMessageMultiThreads;
import haimin.ye.msgConsume.common.queue.producer.ProduceStringMessage;

import java.util.concurrent.BlockingQueue;

public class MessgeConsumeTest2 {

    public static void main(String args[]) {


        BlockingQueue<MessageInterface> queueTest = new MessageBlockingQueue<MessageInterface>(40);
        
        ConsumeMessageMutiThreads aConsumeMessageMutiThreads = new ConsumeMessageMutiThreads(queueTest, ConsumeStringMessage.class, 20);
        try {
            aConsumeMessageMutiThreads.consumeQueue();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        ProduceMessageMultiThreads produceMessageMultiThreads = new ProduceMessageMultiThreads("src/test/java/haimin/ye/paypal/msgConsume/input_publish.txt", queueTest,ProduceStringMessage.class, 8);
        try {
            produceMessageMultiThreads.produceQueue();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
