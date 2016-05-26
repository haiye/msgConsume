package haimin.ye.paypal.msgConsume;

import haimin.ye.msgConsume.common.queue.MessageBlockingQueue;
import haimin.ye.msgConsume.common.queue.consumer.MessageConsumerMultyThreads;
import haimin.ye.msgConsume.common.queue.consumer.StringMessageConsumer;
import haimin.ye.msgConsume.common.queue.message.Message;
import haimin.ye.msgConsume.common.queue.producer.MessageProducerMultyThreads;
import haimin.ye.msgConsume.common.queue.producer.StringMessageProducer;

import java.util.concurrent.BlockingQueue;

public class MessgeConsumeTest2 {

    public static void main(String args[]) {

        BlockingQueue<Message> queueTest = new MessageBlockingQueue<Message>(40);

        MessageConsumerMultyThreads aConsumeMessageMutiThreads = new MessageConsumerMultyThreads(queueTest,
                StringMessageConsumer.class, 20);
        try {
            aConsumeMessageMutiThreads.consumeQueue();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        MessageProducerMultyThreads produceMessageMultiThreads = new MessageProducerMultyThreads(
                "src/test/java/haimin/ye/paypal/msgConsume/input_publish.txt", queueTest, StringMessageProducer.class,
                8);
        try {
            produceMessageMultiThreads.produceQueue();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
