package haimin.ye.msgConsume.common;

public class Message {
    private String aMessage;

    public Message(String aMessage) {
        this.aMessage = aMessage;
    }

    public String getaMessage() {
        return aMessage;
    }

    public void setaMessage(String aMessage) {
        this.aMessage = aMessage;
    }

    @Override
    public String toString() {
        return "Message [aMessage=" + aMessage + "]";
    }

}
