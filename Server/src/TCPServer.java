import Services.SocketService;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class TCPServer {
    private static ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();
    private static ArrayList<Router> routers = new ArrayList<Router>();
    private static ArrayList<RouterConnection> routerConnection = new ArrayList<RouterConnection>();
    private static ServerSocket listenSocket;
    private static RouterEnum routerEnum;

    public static void main (String[] args) throws IOException {
        try{
            routerEnum = RouterEnum.valueOf(args[0]);
            System.out.println(routerEnum.routerPort);
            listenSocket = new ServerSocket(routerEnum.routerPort);


            if (routerEnum.linkRouters.length >= 1) {
                linkWithRouter();
            }

            new Thread(TCPServer::acceptConnections).start();
        } catch(IOException e) {
            System.out.println("Listen:"+e.getMessage());
        }
    }

    private static void linkWithRouter() throws IOException{
        for (RouterEnum linkRouter : routerEnum.linkRouters){
            SocketService socketService = new SocketService();
            socketService.startSocket(linkRouter.routerPort);
            routerConnection.add(new RouterConnection(linkRouter,socketService.getSocket()));
            socketService.send("Olá");
        }
    }

    private static void acceptConnections(){
        try{
            while(true) {
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket, subscribers, routers, routerConnection);
            }
        } catch(IOException e) {
            System.out.println("Listen:"+e.getMessage());
        }
    }
}