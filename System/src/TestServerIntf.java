import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TestServerIntf extends Remote {
    void updateDone() throws RemoteException;
    void getTimestamp(int timestamp) throws RemoteException;
}