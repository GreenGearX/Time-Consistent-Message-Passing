import java.io.IOException;

public class Passer {
    private int id;
    private int numberOfNodes;
    private String[] addresses;
    int nextId;
    int previousId;
    int port;

    Passer(int id, String addresses, int port){
        this.id = id;
        this.addresses = addresses.split(","); //TODO: maybe this is wrong
        this.port = port;
        this.numberOfNodes = this.addresses.length;
        this.nextId = (id + 1) % this.numberOfNodes;
        this.previousId = (id - 1 + this.numberOfNodes) % this.numberOfNodes;
    }

    public void start() throws IOException, ClassNotFoundException{
        System.out.println("id: " + id);
        System.out.println("number of nodes: " + numberOfNodes);
        System.out.println("port: " + port);

        for(int i = 0; i < numberOfNodes; i++){
            System.out.println("address: " + i + ": " + addresses[i]);
        }

        System.out.println("nextId: " + nextId);
        System.out.println("prevId: " + previousId);

        if(id == 0){
            Server server = new Server(port+id);
            server.init();
            Client client = new Client(addresses[nextId], port + nextId);
            client.init();
        }else{
            Client client = new Client(addresses[nextId], port + nextId);
            client.init();
            Server server = new Server(port+id);
            server.init();
        }

    }




}
