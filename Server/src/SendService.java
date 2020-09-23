import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

class SendService {

    void sendMessageToSubscriberByInterest(String interest, String msg) throws IOException {
        System.out.println("Enviando para inscritos");
        Set<Subscriber> subscribers = TCPServer.subscribers.stream()
                .filter(subscriber -> subscriber.getInterest().equals(interest))
                .collect(Collectors.toSet());
        System.out.println(subscribers);
        for (Subscriber subscriber: subscribers) {
            new DataOutputStream(subscriber.getSocket().getOutputStream()).writeUTF(msg);
        }
    }

    void sendMessageToRouterByInterest(String interest, String msg) throws IOException {
        System.out.println("Enviando para routers");
        Set<Router> routers = TCPServer.routers.stream()
                .filter(subscriber -> subscriber.getInterest().equals(interest))
                .collect(Collectors.toSet());
        System.out.println(routers);
        for (Router router: routers) {
            (new DataOutputStream(router.getSocket().getOutputStream())).writeUTF(msg);
        }
    }
}
