import java.io.IOException;

public class program {
    public static void main(String[] args) throws IOException, ClassNotFoundException {


        Passer passer = new Passer(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
        passer.start();
    }
}


