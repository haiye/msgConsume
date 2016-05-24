package haimin.ye.msgConsume.common.queue.publish;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import haimin.ye.msgConsume.common.Constant;
import haimin.ye.msgConsume.common.Message;
import haimin.ye.msgConsume.common.queue.TrackingBlockingQueue;
import haimin.ye.msgConsume.common.queue.consume.ConsumeMessageMutiThreads;
import haimin.ye.msgConsume.common.queue.consume.StrConsumeToFile;

//public class PublishMessage implements Runnable{
public class PublishMessage {

    private static Logger logger = Logger.getLogger("MsgQueue");

    private int numsOfThreads;

    public PublishMessage() {
        this.numsOfThreads = Constant.DEFAULT_NUM_THREADS_PUBLISH;
    }

    public PublishMessage(int numsOfThreads) {
        this.numsOfThreads = numsOfThreads;
    }

    public void publishFromFile(BlockingQueue<Message> queue, String filePath) {
        System.out.println("numsOfThreads = " + numsOfThreads);
        BufferedReader br = null;
        try {
            br = getDriverReader(filePath);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                queue.put(new Message(sCurrentLine));
                System.out.println("new line=" + sCurrentLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                queue.put(Constant.END_TAG);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignore) {
                }
            }
        }

    }

    public void publish(BlockingQueue<Message> queue, Message msg) {
        try {
            queue.put(msg);
            logger.info("begin to publish message into queue");

        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.warning("unable put mssage into queue: " + e);
        } finally {
            logger.info("finished to publish message into queque");

        }
    }

    private BufferedReader getDriverReader(String driverFile) throws FileNotFoundException {
        BufferedReader br;
        StringBuilder sbfp = getFilePath(driverFile);
        File f = new File(sbfp.toString());
        if (f.exists() && !f.isDirectory()) {
            br = new BufferedReader(new FileReader(sbfp.toString()));
        } else {
            br = new BufferedReader(new FileReader(sbfp.insert(0, "src/main/resources/").toString()));
        }
        return br;
    }

    private StringBuilder getFilePath(String driverFilePath) {
        StringBuilder sbfp = new StringBuilder();
        if (!driverFilePath.startsWith("/")) {
            sbfp.append("./");
        }
        sbfp.append(driverFilePath);
        return sbfp;
    }

    public void run() {

    }

}
