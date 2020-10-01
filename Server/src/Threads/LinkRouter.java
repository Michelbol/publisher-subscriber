package Threads;

import Enums.ClientType;
import Enums.Operation;
import Enums.RouterEnum;
import Main.TCPServer;
import Models.Request;
import Models.Router;
import Services.SendService;
import Services.SocketService;
import java.io.IOException;

public class LinkRouter extends Thread {

    private RouterEnum linkRouter;
    private SocketService socketService;
    private Request request;

    public LinkRouter(RouterEnum linkRouter, SocketService socketService) {
        this.linkRouter = linkRouter;
        this.socketService = socketService;
        this.start();
    }

    public void run() {
        try {
            while (!socketService.isClosed()) {
                request = new Request(socketService.receive());
                System.out.println("Recebeu informação de " + linkRouter.name() + " link router: " + request.getMessage());
                if (request.getOperation().equals(Operation.RESPONSE)) {
                    if (request.getType().name().equals(ClientType.ROUTER.name()) && request.getMessage().equals("Initialize")) {
                        System.out.println("Socket inicializado com sucesso com o " + linkRouter.name());
                    }
                    continue;
                }
                SendService sendService = new SendService();
                sendService.sendMessageToRouterByInterest(request);
                if(request.getType().name().equals("PUBLISHER")){
                    sendService.sendMessageToSubscriberByInterest(request);
                }
                if (!request.getType().name().equals("PUBLISHER")) {
                    sendService.sendSubscriberToRouterConnections(request);
                    System.out.println("[Link Models.Router] Adicionado no socket " + request.getFrom() + " um interesse: " + request.getInterest());
                    TCPServer.routers.add(new Router(request.getInterest(), socketService.getSocket(), RouterEnum.valueOf(request.getFrom())));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
