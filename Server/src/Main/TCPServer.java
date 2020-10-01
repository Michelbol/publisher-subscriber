package Main;

import Enums.ClientType;
import Enums.Operation;
import Enums.RouterEnum;
import Models.Request;
import Models.Router;
import Models.RouterConnection;
import Models.Subscriber;
import Services.SocketService;
import Threads.LinkRouter;
import Threads.Message;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class TCPServer {
    public static ArrayList<Subscriber> subscribers = new ArrayList<>();
    public static ArrayList<Router> routers = new ArrayList<>();
    public static ArrayList<RouterConnection> routerConnection = new ArrayList<>();
    public static RouterEnum routerEnum;
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
            SocketService socketService = new SocketService();
            try{
                socketService.startSocket(linkRouter.routerPort);
                TCPServer.routerConnection.add(new RouterConnection(linkRouter, socketService.getSocket()));
                socketService.send(Request.send(ClientType.ROUTER, "", "Initialize", TCPServer.routerEnum.name(), linkRouter.name(), Operation.REQUEST));
            } catch (IOException e) {
                e.printStackTrace();
            }
            new LinkRouter(linkRouter, socketService);
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

    public static boolean canAddNewRouter(String interest, RouterEnum routerEnum){
        return routers.stream().noneMatch(router -> router.getInterest().equals(interest) && router.getRouterEnum() == routerEnum);
    }
}