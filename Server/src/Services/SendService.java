package Services;

import Enums.ClientType;
import Enums.Operation;
import Models.Request;
import Models.Router;
import Models.RouterConnection;
import Models.Subscriber;
import Main.TCPServer;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;
import java.util.stream.Collectors;

public class SendService {

    public void sendMessageToSubscriberByInterest(Request request) throws IOException {
        Set<Subscriber> subscribers = TCPServer.subscribers.stream()
                .filter(subscriber -> subscriber.getInterest().equals(request.getInterest()))
                .collect(Collectors.toSet());
        for (Subscriber subscriber: subscribers) {
            System.out.println("Enviado mensagem para o subscriber: "+subscriber.getName());
            new DataOutputStream(subscriber.getSocket().getOutputStream()).writeUTF(Request.send(ClientType.ROUTER,request.getInterest(),request.getMessage(),request.getTo(),subscriber.getName(), Operation.REQUEST));
        }
    }

    public void sendMessageToRouterByInterest(Request request) throws IOException {
        Set<Router> routers = TCPServer.routers.stream()
                .filter(subscriber -> subscriber.getInterest().equals(request.getInterest()))
                .collect(Collectors.toSet());
        for (Router router: routers) {
            System.out.println("Enviando valor para: "+router.getRouterEnum());
            (new DataOutputStream(router.getSocket().getOutputStream())).writeUTF(Request.send(request.getType(),request.getInterest(),request.getMessage(),request.getTo(),router.getRouterEnum().name(),Operation.REQUEST));
        }
    }

    public void sendSubscriberToRouters(Request request) throws IOException {
        for (RouterConnection routerConnection : TCPServer.routerConnection){
            if (routerConnection != null && !routerConnection.getRouterEnum().name().equals(request.getFrom())){
                System.out.println("Enviando valor para: "+routerConnection.getRouterEnum().name());
                Socket routerSocket = routerConnection.getSocket();
                DataOutputStream out = new DataOutputStream(routerSocket.getOutputStream());
                if (request.getType().equals(ClientType.SUBSCRIBER)){
                    out.writeUTF(Request.send(ClientType.ROUTER, request.getInterest(), request.getMessage(), TCPServer.routerEnum.name(), routerConnection.getRouterEnum().name(), Operation.REQUEST));
                    continue;
                }
                out.writeUTF(Request.send(request.getType(),request.getInterest(),request.getMessage(), TCPServer.routerEnum.name(),routerConnection.getRouterEnum().name(),Operation.REQUEST));
            }
        }
    }
}
