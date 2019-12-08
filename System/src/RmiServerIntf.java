import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerIntf extends Remote {
    String getMessage() throws RemoteException;
    void sendTimestamp(int currentTimestamp) throws RemoteException;
}