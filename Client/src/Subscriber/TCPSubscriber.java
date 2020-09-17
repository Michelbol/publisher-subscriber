package Subscriber;

import Subscriber.Services.SocketService;
import Subscriber.Views.SetupSubscriberView;

import java.io.EOFException;
import java.io.IOException;
import java.net.UnknownHostException;

public class TCPSubscriber {
    public static void main (String[] args) {
        SocketService socketService = new SocketService();
        SetupSubscriberView setupSubscriberView = new SetupSubscriberView();
        String[] config = setupSubscriberView.exec();
        try {
            socketService.startSocket();
            String responseName = socketService.send("name,"+config[0]);
            String responseInterest = socketService.send("interest,"+config[1]);

            System.out.println("Projeto Configurado");
            System.out.println("Resposta 1: "+responseName);
            System.out.println("Resposta 2: "+responseInterest);

        }catch (UnknownHostException e){
            System.out.println("Sock:"+e.getMessage()) ;
        }catch (EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        }catch (IOException e) {
            System.out.println("IO:"+e.getMessage());
        }

    }
}
