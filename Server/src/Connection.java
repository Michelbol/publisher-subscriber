import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

class Connection extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private ArrayList<Subscriber> subscribers;
    private ArrayList<Router> routers;
    private Socket socket;

    Connection(Socket aClientSocket, ArrayList<Subscriber> subscribers, ArrayList<Router> routers) {
        try {
            this.subscribers = subscribers;
            this.routers = routers;
            in = new DataInputStream(aClientSocket.getInputStream());
            out = new DataOutputStream(aClientSocket.getOutputStream());
            this.start();
        } catch(IOException e) {
            System.out.println("Connection:"+e.getMessage());
        }
    }
    public void run(){
        try {
            mapRouters();
            String data = in.readUTF();
            String[] information = data.split("\\|");
            if(information[0].equals(ClientType.SUBSCRIBER.toString())){
                Subscriber subscriber = new Subscriber(information[1], information[2]);
                this.subscribers.add(subscriber);
            } else
            if(information[0].equals(ClientType.PUBLISHER.toString())){
                sendMessageToSubscriberByInterest(information[1], information[2]);
                sendMessageToRouterByInterest(information[1], information[2]);
            }
            if(information[0].equals(ClientType.ROUTER.toString())){
                //todo verificar se o router já existe, via nome
                //Router router = new Router(information[1], information[2], socket);
                //this.routers.add(router);
            }
            out.writeUTF(data);
            System.out.println("Received: "+ data) ;
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

        /*for (Subscriber subscriber: routers) {
            new DataOutputStream(subscriber.getSocket().getOutputStream()).writeUTF(msg);
        }*/
    }

    private void mapRouters(){
        /*if (routerPort.lenght == routers.lenght){

        }*/
    }
}