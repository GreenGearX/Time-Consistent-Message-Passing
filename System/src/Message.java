public class Message implements java.io.Serializable {

    String message;
    int timeStamp;
    int destination;


    Message(String message, int timeStamp, int destination) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.destination = destination;
    }

    public String getMessage(){
        return message;
    }

    public int getTimeStamp(){
        return timeStamp;
    }

    public int getPasserId(){
        return destination;
    }

    public Message clone(){
        return new Message(message,timeStamp,destination);
    }
}
