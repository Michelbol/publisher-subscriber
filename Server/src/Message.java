import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class Message extends Thread{
    private final String data;
    private final Socket clientSocket;

    public Message(String data, Socket clientSocket) {
        this.data = data;
        this.clientSocket = clientSocket;
        this.start();
    }

    static void resolveClient(Request request, Socket socket) throws IOException {
        SendService sendService = new SendService();
        if(request.getType().name().equals(ClientType.SUBSCRIBER.name())){
            System.out.println("Criado novo Inscrito: "+request.getFrom()+" Interesse: "+request.getInterest());
            Subscriber subscriber = new Subscriber(request.getFrom(), request.getInterest(), socket);
            TCPServer.subscribers.add(subscriber);
            sendService.sendSubscriberToRouters(request);
            return;
        }
        System.out.println("Publisher enviou dados para o interesse: "+request.getInterest() + " Mensagem: "+ request.getMessage());
        sendService.sendMessageToSubscriberByInterest(request);
        sendService.sendMessageToRouterByInterest(request);
        sendService.sendSubscriberToRouters(request);
    }

    static void resolveRouter(Request request, Socket socket) throws IOException {
        SendService sendService = new SendService();
        if (request.getMessage().equals("Initialize")){
            System.out.println("Conectado a um novo socket "+request.getFrom());
            TCPServer.routerConnection.add(new RouterConnection(RouterEnum.valueOf(request.getFrom()),socket));
            new ReceiveMessage(socket);
        }
        else {
            RouterEnum from = RouterEnum.valueOf(request.getFrom());
            if(TCPServer.canAddNewRouter(request.getInterest(), from)){
                System.out.println("[Message] Adicionado no socket "+request.getFrom()+" um interesse: "+request.getInterest());
                TCPServer.routers.add(new Router(request.getInterest(), socket, from));
                sendService.sendSubscriberToRouters(request);
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
