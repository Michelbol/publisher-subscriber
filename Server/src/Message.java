import java.io.IOException;
import java.net.Socket;

class Message {

    static void resolve(String[] information, Socket socket) throws IOException {
        if(information[0].equals("RESPONSE")){
            return;
        }
        SendService sendService = new SendService();
        if(information[0].equals(ClientType.SUBSCRIBER.toString())){
            System.out.println("Criado novo Inscrito: "+information[1]);
            Subscriber subscriber = new Subscriber(information[1], information[2], socket);
            TCPServer.subscribers.add(subscriber);
            sendService.sendSubscriberToRouters(information);
        } else
        if(information[0].equals(ClientType.PUBLISHER.toString())){
            System.out.println("Publisher enviou dados para o interesse: "+information[1]);
            sendService.sendMessageToSubscriberByInterest(information[1], information[2]);
            sendService.sendMessageToRouterByInterest(information[1], information[0]+"|"+information[1]+"|"+information[2]);
        } else
        if(information[0].equals(ClientType.ROUTER.toString())){
            if (information[1].equals("Initialize")){
                System.out.println("Conectado a um novo socket "+information[2]);
                TCPServer.routerConnection.add(new RouterConnection(RouterEnum.valueOf(information[2]),socket));
//                new ReceiveMessage(socket);
            }
            else {
                System.out.println("Adicionado a um socket ativo um interesse: "+information[3]);
                TCPServer.routers.add(new Router(information[3], socket, RouterEnum.valueOf(information[1])));
                sendService.sendSubscriberToRouters(information);
            }
        }
    }
}