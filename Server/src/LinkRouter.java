import Services.SocketService;
import java.io.IOException;

public class LinkRouter extends Thread {

    private RouterEnum linkRouter;

    LinkRouter(RouterEnum linkRouter){
        this.linkRouter = linkRouter;
        this.start();
    }

    public void run() {
        SocketService socketService = new SocketService();
        try {
            socketService.startSocket(linkRouter.routerPort);
            TCPServer.routerConnection.add(new RouterConnection(linkRouter, socketService.getSocket()));
            socketService.send(ClientType.ROUTER+"|Initialize|"+TCPServer.routerEnum.name());
            while (!socketService.isClosed()){
                String msg = socketService.receive();
                String[] information = msg.split("\\|");
                System.out.println("Recebeu informação de "+linkRouter.name()+" link router:"+msg);
                if(information[0].equals("RESPONSE")){
                    continue;
                }
                SendService sendService = new SendService();
                if (information[0].equals("PUBLISHER")){
                    sendService.sendMessageToSubscriberByInterest(information[1], information[2]);
                    sendService.sendMessageToRouterByInterest(information[1], information[2]);
                }
                else {
                    sendService.sendMessageToSubscriberByInterest(information[2], information[3]);
                    sendService.sendMessageToRouterByInterest(information[2], information[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
