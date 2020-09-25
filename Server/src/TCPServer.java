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

            acceptConnections();
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
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                String data = in.readUTF();
                new Message(data,clientSocket);
            }
        } catch(IOException e) {
            System.out.println("Listen:"+e.getMessage());
        }
    }

    static String[] splitMessage(String information){
        return information.split("\\|");
    }

    static boolean canAddNewRouter(String interest, RouterEnum routerEnum){
        return routers.stream().noneMatch(router -> router.getInterest().equals(interest) && router.getRouterEnum() == routerEnum);
    }
}