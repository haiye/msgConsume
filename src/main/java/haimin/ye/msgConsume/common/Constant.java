package haimin.ye.msgConsume.common;

import haimin.ye.msgConsume.common.queue.message.Message;

public class Constant {

    public static final Message END_TAG = new Message("END_TAG");

    public static final int DEFAULT_NUM_THREADS_PRODUCER = 10;

    public static final int DEFAULT_NUM_THREADS_CONSUME = 10;

    public static final int DEFAULT_QUEUE_SIZE = 100;

}
