package Subscriber;

import Subscriber.Services.SocketService;
import Subscriber.Views.SetupPublisherView;
import java.io.EOFException;
import java.io.IOException;
import java.net.UnknownHostException;

public class TCPPublisher {
    public static void main (String[] args) {
        int port = Integer.parseInt(args[0]);
        SocketService socketService = new SocketService();
        SetupPublisherView setupPublisherView = new SetupPublisherView();
        String[] config = setupPublisherView.exec();
        try {
            socketService.startSocket(port);
            socketService.send(ClientType.PUBLISHER+"|"+config[0]+"|"+config[1]);
            while (!socketService.isClosed()){
                String receive = socketService.receive();
                System.out.println(receive);
            }
        }catch (UnknownHostException e){
            System.out.println("Sock:"+e.getMessage()) ;
        }catch (EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        }catch (IOException e) {
            System.out.println("IO:"+e.getMessage());
        }
    }
}
