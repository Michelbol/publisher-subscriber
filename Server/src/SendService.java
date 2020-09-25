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
            new DataOutputStream(subscriber.getSocket().getOutputStream()).writeUTF(Request.send(ClientType.ROUTER,request.getInterest(),request.getMessage(),request.getTo(),subscriber.getName(),Operation.REQUEST));
        }
    }

    void sendMessageToRouterByInterest(Request request) throws IOException {
        Set<Router> routers = TCPServer.routers.stream()
                .filter(subscriber -> subscriber.getInterest().equals(request.getInterest()))
                .collect(Collectors.toSet());
        for (Router router: routers) {
            (new DataOutputStream(router.getSocket().getOutputStream())).writeUTF(Request.send(ClientType.ROUTER,request.getInterest(),request.getMessage(),request.getTo(),router.getRouterEnum().name(),Operation.REQUEST));
        }
    }

    void sendSubscriberToRouters(Request request) throws IOException {
        for (RouterConnection routerConnection: TCPServer.routerConnection){
            System.out.println("Primeiro valor: "+routerConnection.getRouterEnum().name());
            System.out.println("Segundo valor:"+request.getFrom());
            if (routerConnection != null && !routerConnection.getRouterEnum().name().equals(request.getFrom())){
                System.out.println("Enviando valor para: "+routerConnection.getRouterEnum().name());
                Socket routerSocket = routerConnection.getSocket();
                DataOutputStream out = new DataOutputStream(routerSocket.getOutputStream());
                out.writeUTF(Request.send(ClientType.ROUTER,request.getInterest(),request.getMessage(),TCPServer.routerEnum.name(),routerConnection.getRouterEnum().name(),Operation.REQUEST));
            }
        }
    }
}
