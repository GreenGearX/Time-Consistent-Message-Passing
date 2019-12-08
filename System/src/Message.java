public class Message {
    String message;
    int timeStamp;
    int passerId;


    Message(String message, int timeStamp, int passerId) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.passerId = passerId;
    }

    public String getMessage(){
        return message;
    }

    public int getTimeStamp(){
        return timeStamp;
    }

    public int getPasserId(){
        return passerId;
    }
}
