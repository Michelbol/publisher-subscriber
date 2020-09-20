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
    private Socket socket;

    Connection(Socket aClientSocket, ArrayList<Subscriber> subscribers) {
        try {
            this.socket = aClientSocket;
            this.subscribers = subscribers;
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
}