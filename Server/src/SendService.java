import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;
import java.util.stream.Collectors;

class SendService {

    void sendMessageToSubscriberByInterest(String interest, String msg) throws IOException {
        Set<Subscriber> subscribers = TCPServer.subscribers.stream()
                .filter(subscriber -> subscriber.getInterest().equals(interest))
                .collect(Collectors.toSet());
        for (Subscriber subscriber: subscribers) {
            new DataOutputStream(subscriber.getSocket().getOutputStream()).writeUTF(msg);
        }
    }

    void sendMessageToRouterByInterest(String interest, String msg) throws IOException {
        Set<Router> routers = TCPServer.routers.stream()
                .filter(subscriber -> subscriber.getInterest().equals(interest))
                .collect(Collectors.toSet());
        for (Router router: routers) {
            (new DataOutputStream(router.getSocket().getOutputStream())).writeUTF(msg);
        }
    }

    void sendSubscriberToRouters(String[] information) throws IOException {
        for (RouterConnection routerConnection: TCPServer.routerConnection){
            Socket routerSocket = routerConnection.getSocket();
            DataOutputStream out = new DataOutputStream(routerSocket.getOutputStream());
            out.writeUTF(ClientType.ROUTER+"|"+TCPServer.routerEnum.name()+"|"+information[1]+"|"+information[2]);
        }
    }
}
