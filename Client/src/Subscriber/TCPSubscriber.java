package Subscriber;

import Subscriber.Services.SocketService;
import Subscriber.Views.SetupSubscriberView;
import java.io.EOFException;
import java.io.IOException;
import java.net.UnknownHostException;

public class TCPSubscriber {
    public static void main (String[] args) {
        RouterEnum routerEnum = RouterEnum.valueOf(args[0]);
        SocketService socketService = new SocketService();
        SetupSubscriberView setupSubscriberView = new SetupSubscriberView();
        String[] config = setupSubscriberView.exec();
        try {
            socketService.startSocket(routerEnum.routerPort);
            socketService.send(ClientType.SUBSCRIBER+"|"+config[0]+"|"+config[1]);
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
