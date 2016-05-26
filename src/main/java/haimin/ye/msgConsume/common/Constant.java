package haimin.ye.msgConsume.common;

import haimin.ye.msgConsume.common.queue.message.Message;
import haimin.ye.msgConsume.common.queue.message.StringMessage;

public class Constant {

    public static final Message END_TAG = new StringMessage("END_TAG");

    public static final int DEFAULT_NUM_THREADS_PRODUCER = 10;

    public static final int DEFAULT_NUM_THREADS_CONSUME = 10;

    public static final int DEFAULT_QUEUE_SIZE = 100;

}
