import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;
import java.util.stream.Collectors;

class SendService {

    void sendMessageToSubscriberByInterest(Request request) throws IOException {
        Set<Subscriber> subscribers = TCPServer.subscribers.stream()
                .filter(subscriber -> subscriber.getInterest().equals(request.getInterest()))
                .collect(Collectors.toSet());
        for (Subscriber subscriber: subscribers) {
            System.out.println("Mensagem: "+request.getMessage());
            new DataOutputStream(subscriber.getSocket().getOutputStream()).writeUTF(Request.send(ClientType.ROUTER,request.getInterest(),request.getMessage(),request.getTo(),subscriber.getName()));
        }
    }

    void sendMessageToRouterByInterest(Request request) throws IOException {
        Set<Router> routers = TCPServer.routers.stream()
                .filter(subscriber -> subscriber.getInterest().equals(request.getInterest()))
                .collect(Collectors.toSet());
        for (Router router: routers) {
            (new DataOutputStream(router.getSocket().getOutputStream())).writeUTF(Request.send(ClientType.ROUTER,request.getInterest(),request.getMessage(),request.getTo(),router.getRouterEnum().name()));
        }
    }

    void sendSubscriberToRouters(Request request) throws IOException {
        for (RouterConnection routerConnection: TCPServer.routerConnection){
            if (routerConnection != null && !routerConnection.getRouterEnum().name().equals(request.getFrom())){
                Socket routerSocket = routerConnection.getSocket();
                DataOutputStream out = new DataOutputStream(routerSocket.getOutputStream());
                out.writeUTF(Request.send(ClientType.ROUTER,request.getInterest(),request.getMessage(),TCPServer.routerEnum.name(),routerConnection.getRouterEnum().name()));
            }
        }
    }
}
