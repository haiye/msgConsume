package haimin.ye.msgConsume.common.msgQueue;

import haimin.ye.msgConsume.common.Constant;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("hiding")
public class TrackingBlockingQueue<Message> extends ArrayBlockingQueue<Message> {
    private static final long serialVersionUID = -3753685985464048165L;
    private int itemsTaken;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public TrackingBlockingQueue(int capacity) {
        super(capacity);
    }

    public int getItemsTaken() throws InterruptedException {
        final Lock lock = this.lock.readLock();
        lock.lockInterruptibly();
        try {
            return itemsTaken;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Message take() throws InterruptedException {
        final Lock lock = this.lock.writeLock();
        lock.lockInterruptibly();
        try {
            final Message item = super.take();
            if(item != Constant.END_TAG) {
                this.itemsTaken++;
            }
            return item;
        } finally {
            lock.unlock();
        }
    }
}
