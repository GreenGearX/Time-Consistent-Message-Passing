import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TestServerIntf extends Remote {
    void getTimestamp(int timestamp) throws RemoteException;
}