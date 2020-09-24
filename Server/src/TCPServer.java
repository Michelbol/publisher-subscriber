import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class TCPServer {
    static ArrayList<Subscriber> subscribers = new ArrayList<>();
    static ArrayList<Router> routers = new ArrayList<>();
    static ArrayList<RouterConnection> routerConnection = new ArrayList<>();
    static RouterEnum routerEnum;
    private static ServerSocket listenSocket;

    public static void main (String[] args) {
        try{
            routerEnum = RouterEnum.valueOf(args[0]);
            listenSocket = new ServerSocket(routerEnum.routerPort);

            if (routerEnum.linkRouters.length >= 1) {
                linkWithRouter();
            }

            new Thread(TCPServer::acceptConnections).start();
        } catch(IOException e) {
            System.out.println("Listen:"+e.getMessage());
        }
    }

    private static void linkWithRouter() {
        for (RouterEnum linkRouter : routerEnum.linkRouters){
            new LinkRouter(linkRouter);
        }
    }

    private static void acceptConnections(){
        try{
            while(true) {
                Socket clientSocket = listenSocket.accept();
                new Connection(clientSocket);
            }
        } catch(IOException e) {
            System.out.println("Listen:"+e.getMessage());
        }
    }

    static String[] splitMessage(String information){
        return information.split("\\|");
    }
}