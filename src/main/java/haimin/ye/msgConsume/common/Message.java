package haimin.ye.msgConsume.common;

public class Message {

    private String fileName;
    private String message;

    public Message(String fileName, String message) {
        this.message = message;
        this.fileName = fileName;
    }
    
    public Message(String message) {
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
