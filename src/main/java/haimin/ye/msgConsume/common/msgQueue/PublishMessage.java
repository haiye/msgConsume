package haimin.ye.msgConsume.common.msgQueue;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;



import haimin.ye.msgConsume.common.Message;

public class PublishMessage {
    private static Logger logger = Logger.getLogger("MsgQueue");

 
    
    public void put(BlockingQueue<Message>  queue, Message msg){
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.warning("unable put mssage into queue: "+e);
        }finally{
            logger.info("yehaimin");
            
        }
    }

    public Message get(BlockingQueue<Message>  queue){
        
        return null;
    }
    
    
    public static void main(String args[]){
        Logger logger = Logger.getLogger("MsgQueu22222e");
        logger.info("yehaimin");

        logger.warning("hm");
    }
}
