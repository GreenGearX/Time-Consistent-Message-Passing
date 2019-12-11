import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerIntf extends Remote {
    void sendEnvelope(Envelope envelope) throws RemoteException;
}