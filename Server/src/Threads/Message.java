package Threads;

import Enums.ClientType;
import Enums.Operation;
import Enums.RouterEnum;
import Main.TCPServer;
import Models.Request;
import Models.Router;
import Models.RouterConnection;
import Models.Subscriber;
import Services.SendService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Message extends Thread{
    private final String data;
    private final Socket clientSocket;

    public Message(String data, Socket clientSocket) {
        this.data = data;
        this.clientSocket = clientSocket;
        this.start();
    }

    private static void resolveClient(Request request, Socket socket) throws IOException {
        SendService sendService = new SendService();
        if(request.getType().name().equals(ClientType.SUBSCRIBER.name())){
            System.out.println("Criado novo Inscrito: "+request.getFrom()+" Interesse: "+request.getInterest());
            Subscriber subscriber = new Subscriber(request.getFrom(), request.getInterest(), socket);
            TCPServer.subscribers.add(subscriber);
            sendService.sendSubscriberToRouterConnections(request);
            return;
        }
        System.out.println("Publisher enviou dados para o interesse: "+request.getInterest() + " Mensagem: "+ request.getMessage());
        sendService.sendMessageToSubscriberByInterest(request);
        sendService.sendMessageToRouterByInterest(request);
        if(!request.getType().name().equals(ClientType.PUBLISHER.name())){
            sendService.sendSubscriberToRouterConnections(request);
        }
    }

    static void resolveRouter(Request request, Socket socket) throws IOException {
        SendService sendService = new SendService();
        if (request.getMessage().equals("Initialize")){
            System.out.println("Conectado a um novo socket "+request.getFrom());
            TCPServer.routerConnection.add(new RouterConnection(RouterEnum.valueOf(request.getFrom()),socket));
            new ReceiveMessage(socket);
            return;
        }
        RouterEnum from = RouterEnum.valueOf(request.getFrom());
        if(request.getType().equals(ClientType.PUBLISHER)){
            System.out.println("Recebido Mensagem Publisher: "+ request.getMessage()+" - Interesse:"+request.getInterest());
            sendService.sendMessageToSubscriberByInterest(request);
            sendService.sendMessageToRouterByInterestWithoutOrigin(request);
        }else{
            if(TCPServer.canAddNewRouter(request.getInterest(), from)){
                System.out.println("[Threads.Message] Adicionado no socket "+request.getFrom()+" um interesse: "+request.getInterest());
                TCPServer.routers.add(new Router(request.getInterest(), socket, from));
                sendService.sendSubscriberToRouterConnections(request);
            }else{
                System.out.println("Mensagem não mapeada");
                System.out.println(request.toString());
            }
        }
    }

    public void run(){
        try{
            Request request = new Request(this.data);
            DataOutputStream out = new DataOutputStream(this.clientSocket.getOutputStream());

            if(request.getOperation().equals(Operation.RESPONSE)){
                return;
            }

            if(request.getType().name().equals(ClientType.ROUTER.name())) {
                resolveRouter(request, this.clientSocket);
                out.writeUTF(Request.send(request.getType(),request.getInterest(),request.getMessage(),request.getFrom(),request.getTo(),Operation.RESPONSE));
                return;
            }

            resolveClient(request, clientSocket);
            out.writeUTF(Request.send(request.getType(),request.getInterest(),request.getMessage(),request.getFrom(),request.getTo(),Operation.RESPONSE));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
