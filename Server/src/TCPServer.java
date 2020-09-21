import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class TCPServer {
    private static ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();
    private static ServerSocket listenSocket;
    private static Socket outSocket;

    public static void main (String[] args) throws IOException {
        try{
            int serverPort = Integer.parseInt(args[0]);
            listenSocket = new ServerSocket(serverPort);
            new Thread(TCPServer::acceptConnections).start();
        } catch(IOException e) {
            System.out.println("Listen:"+e.getMessage());
        }
    }

    private static void acceptConnections(){
        try{
            while(true) {
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket, subscribers);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void watchRouter(){
        try{
            while (!outSocket.isClosed()){
                DataInputStream in = new DataInputStream(outSocket.getInputStream());
                String receive =  in.readUTF();
                System.out.println(receive);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}