import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.concurrent.atomic.AtomicInteger;


public class TestServer extends UnicastRemoteObject implements TestServerIntf {

    static AtomicInteger nodesWorking;

    public void getTimestamp(int timestamp) throws RemoteException{
        System.out.println(timestamp);
        System.out.flush();
    }

    public void updateDone() throws RemoteException{
        nodesWorking.getAndDecrement();
    }

    public TestServer() throws RemoteException {
        super(0);
    }

    public static void main(String args[]) throws Exception {
        nodesWorking = new AtomicInteger(Integer.parseInt(args[0]));

        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
        }


        TestServer server = new TestServer();
        Naming.rebind("//localhost/" + "test", server);

        while(nodesWorking.get() > 0){
            Thread.sleep(500);
        }

        System.exit(0);
    }

}
