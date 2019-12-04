import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.io.*;

public class Client {
    int port;
    Socket socket;
    String host;

    Client(String host, int port){
        this.port = port;
        this.host = host;
    }

    public void init() throws IOException {
        while(true){
            try {
                socket = new Socket(host, port);
                break;
            } catch (Exception e){}
        }
        OutputStream output = new PrintStream(socket.getOutputStream());
        ObjectOutputStream object_output = new ObjectOutputStream(output);
//        while(true) {
//            output.println("hahahahaha");
//        }
        Message mes = new Message("hello", 999, 8);
        object_output.writeObject(mes);
    }

}
