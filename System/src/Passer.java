public class Passer {
    private int id;
    private int numberOfNodes;
    private String[] addresses;
    int nextId;
    int previousId;
    int port;

    Passer(int id, int numberOfNodes, String addresses, int port){
        this.id = id;
        this.addresses = addresses.split(","); //TODO: maybe this is wrong
        this.port = port;
        this.numberOfNodes = numberOfNodes;
//        this.nextId = nextId;
//        this.previousId = previousId;
    }

    public void start(){
        System.out.println("id: " + id);
        System.out.println("number of nodes: " + numberOfNodes);
        System.out.println("port: " + port);

        for(int i = 0; i < numberOfNodes; i++){
            System.out.println("address: " + i + ": " + addresses[i]);
        }



    }




}
