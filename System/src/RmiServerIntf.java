import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerIntf extends Remote {
    String getMessage() throws RemoteException;
    void sendTimestamp(int id, int currentTimestamp, boolean done) throws RemoteException;
    void sendMessage(Message message) throws RemoteException;
}