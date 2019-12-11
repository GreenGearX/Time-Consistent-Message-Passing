import java.io.Serializable;

public class Message implements Serializable {

    public String message;
    public int timeStamp;
    public int destination;

    Message(String message, int timeStamp, int destination) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.destination = destination;
    }

    public Message clone(){
        return new Message(message,timeStamp,destination);
    }
}
