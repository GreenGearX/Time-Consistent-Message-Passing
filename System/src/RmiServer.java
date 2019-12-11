import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Random;

public class RmiServer extends UnicastRemoteObject implements RmiServerIntf {
    static Queue<Message> messages = new LinkedList<>();
    static int nextNodeId;
    static int id;
    static int numberOfNodes;
    static int numberOfMessages;
    static RmiServerIntf nextNode;
    static TestServerIntf testServer;
    static NodeInfo[] nodes;
    static ConcurrentLinkedQueue<Envelope> inbox = new ConcurrentLinkedQueue<>();
    static ConcurrentLinkedQueue<Envelope> outbox = new ConcurrentLinkedQueue<>();
    static int send_messages = 0;
    static int received_confirmations = 0;
    static String testServerIP;
    static String nextNodeIP;
    static String ownIP;


    public RmiServer() throws RemoteException {
        super(0); // required to avoid the 'rmic' step, see below
    }

    public void sendEnvelope(Envelope envelope) throws RemoteException {
        inbox.add(envelope.clone());
    }

    static public void processEnvelope(Envelope envelope) throws RemoteException {
        switch (envelope.type) {
            case Envelope.MESSAGE:
                handleMessage(envelope);
                break;
            case Envelope.CONFIRMATION:
                handleConfirmation(envelope);
                break;
            case Envelope.UPDATE:
                handleUpdate(envelope);
                break;
        }
    }

    static public void handleConfirmation(Envelope envelope) throws RemoteException{
        if (envelope.destination == id) { // if the confirmation reaches its destination, update the timestamp to the next message
            updateTimestamp();
            received_confirmations++;
        } else {
            //otherwise, relay the confirmation
            outbox.add(envelope.clone());
        }
    }

    static public void handleMessage(Envelope envelope) throws RemoteException {
        if (envelope.destination == id) { // if the message has arrived at the destination, send a confirmation back
            processMessage(envelope.message);
            outbox.add(new Envelope(null, envelope.senderInfo.id, envelope.senderInfo.clone(), Envelope.CONFIRMATION));
        } else {
            // otherwise, relay the message to the next node
            outbox.add(envelope.clone());
        }
    }

    static public void handleUpdate(Envelope envelope) {
        if (envelope.senderInfo.id == id) { // if the update has arrived back at the node which sent it, ignore it
            received_confirmations++;
        } else {
            // otherwise, change info on the sender and relay the update
            nodes[envelope.senderInfo.id].nextTimestamp = envelope.senderInfo.nextTimestamp;
            nodes[envelope.senderInfo.id].done = envelope.senderInfo.done;

            outbox.add(envelope.clone());
        }
    }

    static public void processMessage(Message message) throws RemoteException {
        testServer.getTimestamp(message.timeStamp);
    }


    public static void main(String args[]) throws Exception {

        id = Integer.parseInt(args[0]);
        numberOfNodes = Integer.parseInt(args[1]);
        numberOfMessages = Integer.parseInt(args[2]);
        nextNodeId = (id + 1) % numberOfNodes;

        Random rand = new Random(999);
        nodes = new NodeInfo[numberOfNodes];

        testServerIP = args[4];
        nextNodeIP = args[3];
        ownIP = args[5];

        for (int i = 0; i < numberOfNodes; i++) {
            nodes[i] = new NodeInfo(i);
        }

        // generate messages
        {int i = 0;
        while(messages.size() < numberOfMessages / numberOfNodes) {

            if (id == rand.nextInt(numberOfNodes + 20)) {
                messages.add(new Message("test", i, i));
                System.out.println(i);

            }
            i++;
        }}


        // listen for previous node
        try { //special exception handler for registry creation
            LocateRegistry.createRegistry(1099 + id);
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
        }

        //Instantiate RmiServer
        RmiServer server = new RmiServer();
        Naming.rebind("//"+ ownIP+"/" + args[0], server);

        // connect to next node
        while (true) {
            try {
                nextNode = (RmiServerIntf) Naming.lookup("//" + nextNodeIP + "/" + nextNodeId);
                break;
            } catch (Exception e) {
            }
        }

        // connect to test server
        while (true) {
            try {
                testServer = (TestServerIntf) Naming.lookup("//"+ testServerIP +"/" + "test");
                break;
            } catch (Exception e) {
            }
        }

        // broadcast initial nextTimeStamp
        updateTimestamp();

        while (true) {

            while (inbox.size() > 0) {
                processEnvelope(inbox.remove());
            }

            while (outbox.size() > 0) {
                nextNode.sendEnvelope(outbox.remove());
            }

            if (messages.size() > 0 && !nodes[id].done && nodes[id].nextTimestamp == minTimestamp(nodes) && nodes[id].nextTimestamp == messages.peek().timeStamp) {
                outbox.add(new Envelope(messages.peek(), nodeDestination(messages.remove().destination), nodes[id].clone(), Envelope.MESSAGE));
                send_messages++;
            }

            if (allNodesDone(nodes) && inbox.size() == 0 && outbox.size() == 0 && send_messages == received_confirmations) {
                System.exit(0);
            }

        }

    }

    static void updateTimestamp() throws RemoteException{
        if (messages.size() > 0) { // if there is a message in the queue, set the next timestamp
            nodes[id].nextTimestamp = messages.peek().timeStamp;
        } else { // otherwise, the node is done
            testServer.updateDone();
            nodes[id].done = true;
        }

        outbox.add(new Envelope(null, id, nodes[id].clone(), Envelope.UPDATE));
        send_messages++;
    }

    static int minTimestamp(NodeInfo[] nodes) {
        int result = nodes[id].nextTimestamp;
        for (int i = 0; i < nodes.length; i++) {
            if (!nodes[i].done) {
                result = Integer.min(result, nodes[i].nextTimestamp);
            }
        }
        return result;
    }

    static int nodeDestination(int destination) {
        return destination % numberOfNodes;
    }

    static boolean allNodesDone(NodeInfo[] nodes) {
        boolean allDone = true;

        for (int i = 0; i < nodes.length; i++) {
            allDone = allDone && nodes[i].done;
        }
        return allDone;
    }

}