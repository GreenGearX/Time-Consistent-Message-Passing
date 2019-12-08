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
//    static AtomicInteger currentTimeStamp = new AtomicInteger(0);
    static int nextNodeId;
    static int id;
    static int numberOfNodes;
    static RmiServerIntf server1;
    static TestServerIntf server2;
    static AtomicBoolean currentTimestampChange = new AtomicBoolean(false);
    static NodeInfo[] nodes;
    static Queue<Message> messages = new LinkedList<>();
    static int sentTimestamp = -1;


    public RmiServer() throws RemoteException {
        super(0); // required to avoid the 'rmic' step, see below
    }

    public String getMessage() {
        return MESSAGE;
    }

    public void sendMessage(Message message) throws RemoteException {
        if(message.getTimeStamp() == sentTimestamp){
            nodes[id].currentTimestampChange.set(true);
        } else {
            if (isAtDestination(message.destination)) {
                System.out.println(message.getMessage());
                messages.add(message.clone());
            } else {
                messages.add(message.clone());
            }
        }

    }


    public void sendTimestamp(int id, int currentTimestamp, boolean done) throws RemoteException {
        if (!nodes[id].done.get() && done) {
            nodes[id].done.set(true);
            nodes[id].currentTimestampChange.set(true);
        }
        if (nodes[id].currentTimestamp.get() < currentTimestamp) {
            nodes[id].currentTimestamp.set(currentTimestamp);
            nodes[id].currentTimestampChange.set(true);
        }
    }

    public static void main(String args[]) throws Exception {


        Queue<Integer> q = new LinkedList<>();

        id = Integer.parseInt(args[0]);
        numberOfNodes = Integer.parseInt(args[2]);
        Random rand = new Random(999);

        nodes = new NodeInfo[numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            nodes[i] = new NodeInfo();
        }

        for (int i = 0; i < 100; i++) {
            if (id == rand.nextInt(numberOfNodes + 20)) {
                q.add(i);
                System.out.println("added: " + i);
            }
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////
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


        while (true) {
            try {
                server1 = (RmiServerIntf) Naming.lookup("//localhost/" + args[1]);
                break;
            } catch (Exception e) {
            }
        }

        while (true) {
            try {
                server2 = (TestServerIntf) Naming.lookup("//localhost/" + "test");
                break;
            } catch (Exception e) {
            }
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////
        nodes[id].currentTimestamp.set(q.peek());
        server1.sendTimestamp(id, nodes[id].currentTimestamp.get(), nodes[id].done.get());

        while (true) {
            if (q.size() > 0 && minTimestamp(nodes) == q.peek()) {

                sentTimestamp = q.peek();
                server1.sendMessage(new Message("hey" + q.peek(), q.peek(), q.remove()));
//                server2.getTimestamp(q.remove());
                if (q.size() > 0) {
                    nodes[id].currentTimestamp.set(q.peek());
//                    server1.sendTimestamp(id, nodes[id].currentTimestamp.get(), nodes[id].done.get());
                }

            } else if (q.size() == 0) {
                nodes[id].done.set(true);
                server1.sendTimestamp(id, nodes[id].currentTimestamp.get(), nodes[id].done.get());
            }
            for (int i = 0; i < numberOfNodes; i++) {
                if (nodes[i].currentTimestampChange.get()) {
                    nodes[i].currentTimestampChange.set(false);
                    server1.sendTimestamp(i, nodes[i].currentTimestamp.get(), nodes[id].done.get());
                }
            }
            if(messages.size()> 0){
                server1.sendMessage(messages.remove());
            }
        }

    }

    static int minTimestamp(NodeInfo[] nodes) {
        int result = nodes[id].currentTimestamp.get();
        for (int i = 0; i < nodes.length; i++) {
            if (!nodes[i].done.get()) {
                result = Integer.min(result, nodes[i].currentTimestamp.get());
            }
        }

        return result;
    }

    static boolean isAtDestination(int destination) {
        return destination % numberOfNodes == id;
    }
}