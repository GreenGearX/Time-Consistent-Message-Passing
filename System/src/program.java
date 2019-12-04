import java.io.IOException;

public class program {
    public static void main(String[] args) throws IOException, ClassNotFoundException {


//        System.out.println("Hello World.");
//        for(int i = 0; i < args.length; i++){
//            System.out.println(args[i]);
//        }
//        if(args[0].equals("bla")) {
//            Server server = new Server(9000);
//            server.init();
//        } else {
//            Client client = new Client("127.0.0.1", 9000);
//            client.init();
//        }
        //id, numberOfNodes, addresses, nextId, previousId, port

        Passer passer = new Passer(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
        passer.start();
    }
}


