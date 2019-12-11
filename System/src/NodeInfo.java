import java.io.Serializable;

public class NodeInfo implements Serializable {
    public int id;
    public int nextTimestamp;
    public boolean done;

    public NodeInfo(int id, int nextTimestamp, boolean done){
        this.id = id;
        this.nextTimestamp = nextTimestamp;
        this.done = done;
    }

    public NodeInfo(int id){
        this.id = id;
        this.nextTimestamp = 0;
        this.done = false;
    }

    public NodeInfo clone(){
        return new NodeInfo(id, nextTimestamp, done);
    }

}
