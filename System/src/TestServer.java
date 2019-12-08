import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;



public class TestServer extends UnicastRemoteObject implements TestServerIntf {

    public void getTimestamp(int timestamp) throws RemoteException{
        System.out.println(timestamp);
        System.out.flush();
    }
    public TestServer() throws RemoteException {
        super(0);
    }

    public static void main(String args[]) throws Exception {
//        System.out.println("RMI server started");

        try { //special exception handler for registry creation
            LocateRegistry.createRegistry(1099);
//            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
//            System.out.println("java RMI registry already exists.");
        }

        //Instantiate RmiServer
        TestServer server = new TestServer();

        // Bind this object instance to the name "RmiServer"
        Naming.rebind("//localhost/" + "test", server);
//        System.out.println("PeerServer bound in registry");
    }

}
