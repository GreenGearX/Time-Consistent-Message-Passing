import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;

public class RmiServer extends UnicastRemoteObject implements RmiServerIntf {
    public static final String MESSAGE = "Hello World";

    public RmiServer() throws RemoteException {
        super(0); // required to avoid the 'rmic' step, see below
    }

    public String getMessage() {
        return MESSAGE;
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

        RmiServerIntf server1;
        while(true) {
            try {
                server1 = (RmiServerIntf) Naming.lookup("//localhost/" + args[1]);
                break;
            } catch (Exception e) {
            }
        }
        System.out.println(server1.getMessage());


//        while(true){
//            System.out.println("Still alive");
//            Thread.sleep(500);
//        }
    }
}