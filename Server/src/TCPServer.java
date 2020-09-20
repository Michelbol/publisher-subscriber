import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class TCPServer {
    private static ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();
    static ArrayList interest = new ArrayList();
    public static void main (String[] args) {
        try{
            int serverPort = Integer.parseInt(args[0]);
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while(true) {
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket, subscribers);
//                this.subscribers.stream().filter(subscriber -> subscriber.getInterest().equals(information[2]));
            }
        } catch(IOException e) {
            System.out.println("Listen:"+e.getMessage());
        }
    }
}