import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

public class RmiServer extends UnicastRemoteObject implements RmiServerIntf {
    public static final String MESSAGE = "Hello World";
//    static int currentTimeStamp = 0;
    static AtomicInteger currentTimeStamp = new AtomicInteger(0);
    static int nextNodeId;
    static int id;
    static RmiServerIntf server1;
    static AtomicBoolean currentTimestampChange = new AtomicBoolean(false);

    public RmiServer() throws RemoteException {
        super(0); // required to avoid the 'rmic' step, see below
    }

    public String getMessage() {
        return MESSAGE;
    }

    public void sendTimestamp(int currentTimestamp) throws RemoteException {
        if(this.currentTimeStamp.get() < currentTimestamp){
            this.currentTimeStamp.set(currentTimestamp);
            currentTimestampChange.set(true);
        }
    }

    public static void main(String args[]) throws Exception {
        System.out.println("RMI server started");

        try { //special exception handler for registry creation
            LocateRegistry.createRegistry(1099);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            System.out.println("java RMI registry already exists.");
        }

        //Instantiate RmiServer
        RmiServer server = new RmiServer();

        // Bind this object instance to the name "RmiServer"
        Naming.rebind("//localhost/" + args[0], server);
        System.out.println("PeerServer bound in registry");


        while(true) {
            try {
                server1 = (RmiServerIntf) Naming.lookup("//localhost/" + args[1]);
                break;
            } catch (Exception e) {
            }
        }
//        System.out.println(server1.getMessage());


        Queue<Integer> q = new LinkedList<>();

//        for(int i = Integer.parseInt(args[0]); i < 10000; i+=3){
//            q.add(i);
//        }
        id = Integer.parseInt(args[0]);
        Random rand = new Random(999);

        for(int i = 0; i < 1000; i++){
            if(id == rand.nextInt(3)){
                q.add(i);
                System.out.println("added: " + i);
            }
        }

//        nextNodeId = Integer.parseInt(args[1]);



        while(true){
            if(q.size() > 0 && currentTimeStamp.get() == q.peek()) {
                System.out.println("popped: " + q.remove());
                server1.sendTimestamp(currentTimeStamp.incrementAndGet());
            }

            if(currentTimestampChange.get()){
                currentTimestampChange.set(false);
                server1.sendTimestamp(currentTimeStamp.get());
            }
        }



//        while(true){
//            System.out.println("Still alive");
//            Thread.sleep(500);
//        }
    }
}