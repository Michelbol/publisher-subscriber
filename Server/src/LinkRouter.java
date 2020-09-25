import Services.SocketService;

import java.io.IOException;

public class LinkRouter extends Thread {

    private RouterEnum linkRouter;

    LinkRouter(RouterEnum linkRouter) {
        this.linkRouter = linkRouter;
        this.start();
    }

    public void run() {
        SocketService socketService = new SocketService();
        try {
            socketService.startSocket(linkRouter.routerPort);
            TCPServer.routerConnection.add(new RouterConnection(linkRouter, socketService.getSocket()));
            socketService.send(Request.send(ClientType.ROUTER, "", "Initialize", TCPServer.routerEnum.name(), linkRouter.name(), Operation.REQUEST));
            while (!socketService.isClosed()) {
                Request request = new Request(socketService.receive());
                System.out.println("Recebeu informação de " + linkRouter.name() + " link router: " + request.getMessage());
                if (request.getOperation().equals(Operation.RESPONSE)) {
                    if (request.getType().name().equals(ClientType.ROUTER.name()) && request.getMessage().equals("Initialize")) {
                        System.out.println("Socket inicializado com sucesso com o " + linkRouter.name());
                    }
                    continue;
                }
                SendService sendService = new SendService();
                sendService.sendMessageToSubscriberByInterest(request);
                sendService.sendMessageToRouterByInterest(request);
                sendService.sendSubscriberToRouters(request);
                if (!request.getType().name().equals("PUBLISHER")) {
                    System.out.println("[Link Router] Adicionado no socket " + request.getFrom() + " um interesse: " + request.getInterest());
                    TCPServer.routers.add(new Router(request.getInterest(), socketService.getSocket(), RouterEnum.valueOf(request.getFrom())));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
