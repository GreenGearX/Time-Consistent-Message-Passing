import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class NodeInfo {

    public AtomicInteger currentTimestamp = new AtomicInteger(0);
    public AtomicBoolean currentTimestampChange = new AtomicBoolean(false);
    public AtomicBoolean done = new AtomicBoolean(false);

}
