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
                String[] information = TCPServer.splitMessage(msg);
                System.out.println("Recebeu informação de "+linkRouter.name()+" link router:"+msg);
                if(information[0].equals("RESPONSE")){
                    if(information[1].equals(ClientType.ROUTER.toString()) && information[2].equals("Initialize")){
                        System.out.println("Socket inicializado com sucesso com o "+linkRouter.name());
                    }
                    continue;
                }
                SendService sendService = new SendService();
                String sendMsg;
                String sendInterest;
                if (information[0].equals("PUBLISHER")){
                    sendMsg = information[1];
                    sendInterest = information[2];
                } else {
                    sendMsg = information[2];
                    sendInterest = information[3];
                }
                sendService.sendMessageToSubscriberByInterest(sendMsg, sendInterest);
                sendService.sendMessageToRouterByInterest(sendMsg, sendInterest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
