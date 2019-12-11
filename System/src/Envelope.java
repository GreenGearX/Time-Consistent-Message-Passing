import java.io.Serializable;

public class Envelope implements Serializable {
    public static final int MESSAGE = 0, CONFIRMATION = 1, UPDATE = 2;

    public Message message;
    public int destination;
    public NodeInfo senderInfo;
    public int type;

    public Envelope(Message message, int destination, NodeInfo senderInfo, int type){
        this.message = message;
        this.destination = destination;
        this.senderInfo = senderInfo;
        this.type = type;
    }

    public Envelope clone(){
        if(message != null && senderInfo != null){
            return new Envelope(message.clone(), destination, senderInfo.clone(), type);
        } else {
            return new Envelope(null, destination, senderInfo.clone(), type);
        }
    }
}
