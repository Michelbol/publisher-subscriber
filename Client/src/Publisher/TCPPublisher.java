package Publisher;

import Enums.ClientType;
import Enums.Operation;
import Subscriber.Request;
import Enums.RouterEnum;
import Services.SocketService;
import Publisher.Views.SetupPublisherView;

import java.io.EOFException;
import java.io.IOException;
import java.net.UnknownHostException;

public class TCPPublisher {
    public static void main (String[] args) {
        RouterEnum routerEnum = RouterEnum.valueOf(args[0]);
        SocketService socketService = new SocketService();
        SetupPublisherView setupPublisherView = new SetupPublisherView();
        String[] config = setupPublisherView.exec();
        try {
            socketService.startSocket(routerEnum.routerPort);
            socketService.send(Request.send(ClientType.PUBLISHER,config[0],config[1],"",routerEnum.name(), Operation.REQUEST));
            while (!socketService.isClosed()){
                System.out.println("Recebido Mensagem: "+socketService.receive());
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
