public class Message implements java.io.Serializable {
    String message;
    int timeStamp;

    Message(String message, int timeStamp) {
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getMessage(){
        return message;
    }

    public int getTimeStamp(){
        return timeStamp;
    }
}
