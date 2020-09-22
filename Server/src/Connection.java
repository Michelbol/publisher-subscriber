import Services.SocketService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

class Connection extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private ArrayList<Subscriber> subscribers;
    private ArrayList<Router> routers;
    private ArrayList<RouterConnection> routerConnections;
    private Socket socket;

    Connection(Socket aClientSocket, ArrayList<Subscriber> subscribers, ArrayList<Router> routers, ArrayList<RouterConnection> routerConnections) {
        try {
            this.subscribers = subscribers;
            this.routers = routers;
            this.socket = aClientSocket;
            this.routerConnections = routerConnections;
            in = new DataInputStream(aClientSocket.getInputStream());
            out = new DataOutputStream(aClientSocket.getOutputStream());
            this.start();
        } catch(IOException e) {
            System.out.println("Connection:"+e.getMessage());
        }
    }
    public void run(){
        try {
            String data = in.readUTF();
            String[] information = data.split("\\|");

            if(information[0].equals(ClientType.SUBSCRIBER.toString())){
                Subscriber subscriber = new Subscriber(information[1], information[2], socket);
                this.subscribers.add(subscriber);
                for (RouterConnection routerConnection: routerConnections){
                    Socket routerSocket = routerConnection.getSocket();
                    DataOutputStream out = new DataOutputStream(routerSocket.getOutputStream());
                    out.writeUTF(ClientType.ROUTER+"|"+information[1]+"|"+information[2]);
                }
            } else
            if(information[0].equals(ClientType.PUBLISHER.toString())){
                sendMessageToSubscriberByInterest(information[1], information[2]);
                sendMessageToRouterByInterest(information[1], information[2]);
            } else
            if(information[0].equals(ClientType.ROUTER.toString())){
                if (information[1].equals("Initialize")){
                    routerConnections.add(new RouterConnection(RouterEnum.valueOf(information[2]),socket));
                }
                else routers.add(new Router(information[2],socket));
            }
            out.writeUTF(data);
            while(!this.socket.isClosed()){
                data = in.readUTF();
                out.writeUTF(data);
            }
        } catch(EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        } catch(IOException e) {
            System.out.println("IO:"+e.getMessage());
        }
    }

    public ArrayList<Subscriber> getSubscribers() {
        return subscribers;
    }

    private void sendMessageToSubscriberByInterest(String interest, String msg) throws IOException {
        Set<Subscriber> subscribers = this.subscribers.stream()
                .filter(subscriber -> subscriber.getInterest().equals(interest))
                .collect(Collectors.toSet());

        for (Subscriber subscriber: subscribers) {
            new DataOutputStream(subscriber.getSocket().getOutputStream()).writeUTF(msg);
        }
    }

    private void sendMessageToRouterByInterest(String interest, String msg) throws IOException {
        Set<Router> routers = this.routers.stream()
                .filter(subscriber -> subscriber.getInterest().equals(interest))
                .collect(Collectors.toSet());

        for (Router router: routers) {
            new DataOutputStream(router.getSocket().getOutputStream()).writeUTF(msg);
        }
    }
}