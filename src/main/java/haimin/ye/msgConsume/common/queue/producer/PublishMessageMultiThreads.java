package haimin.ye.msgConsume.common.queue.producer;

import haimin.ye.msgConsume.common.Constant;
import haimin.ye.msgConsume.common.queue.message.MessageInterface;
import haimin.ye.msgConsume.common.queue.message.StringMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

//public class PublishMessage implements Runnable{
public class PublishMessageMultiThreads {

    private static Logger logger = Logger.getLogger("MsgQueue");

    private int numsOfThreads;

    public PublishMessageMultiThreads() {
        this.numsOfThreads = Constant.DEFAULT_NUM_THREADS_PRODUCER;
    }

    public PublishMessageMultiThreads(int numsOfThreads) {
        this.numsOfThreads = numsOfThreads;
    }

    public void publishFromFile(BlockingQueue<MessageInterface> queue, String filePath) {
        System.out.println("numsOfThreads = " + numsOfThreads);
        BufferedReader br = null;
        try {
            br = getDriverReader(filePath);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                queue.put(new StringMessage(sCurrentLine));
//                System.out.println("new line=" + sCurrentLine);
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

    public void publish(BlockingQueue<MessageInterface> queue, MessageInterface msg) {
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
