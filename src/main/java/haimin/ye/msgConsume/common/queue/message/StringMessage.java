package haimin.ye.msgConsume.common.queue.message;

import haimin.ye.msgConsume.common.queue.message.Message;

public class StringMessage implements Message {

    private String fileName;
    private String message;

    public StringMessage(String fileName, String message) {
        this.message = message;
        this.fileName = fileName;
    }

    public StringMessage(String message) {
        this.fileName = null;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Message [fileName=" + fileName + ", message=" + message + "]";
    }

}
