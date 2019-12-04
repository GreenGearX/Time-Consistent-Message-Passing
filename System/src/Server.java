import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    int port;
    ServerSocket serverSocket;

    Server(int portNumber){
        port = portNumber;
    }

    public void init() throws IOException, ClassNotFoundException {
            serverSocket = new ServerSocket(port) {
            protected void finalize() throws IOException {
                this.close();
            }
        };

        Socket clientSocket = serverSocket.accept();
//        PrintWriter out =
//                new PrintWriter(clientSocket.getOutputStream(), true);
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(clientSocket.getInputStream()));
        InputStream is = clientSocket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Message obj1=(Message)ois.readObject();
//        while(true){
//            System.out.println(in.readLine());
//        }
        System.out.println("Message: " + obj1.getMessage());
        System.out.println("Timestamp: " + obj1.getTimeStamp());

    }

//    public void printIncoming(){
//
//    }
}
