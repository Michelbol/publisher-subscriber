import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

class Connection extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;

    Connection(Socket aClientSocket) {
        try {
            this.socket = aClientSocket;
            in = new DataInputStream(aClientSocket.getInputStream());
            out = new DataOutputStream(aClientSocket.getOutputStream());
            this.start();
        } catch(IOException e) {
            System.out.println("Connection:"+e.getMessage());
        }
    }
    public void run(){
        try {
            while(!this.socket.isClosed()){
                String data = in.readUTF();
                this.resolveMessage(data.split("\\|"));
                out.writeUTF("RESPONSE|"+data);
            }
        } catch(EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        } catch(IOException e) {
            System.out.println("IO:"+e.getMessage());
        }
    }

    private void resolveMessage(String[] information) throws IOException {
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
            }
            else {
                System.out.println("Adicionado a um socket ativo um interesse: "+information[3]);
                TCPServer.routers.add(new Router(information[3], socket, RouterEnum.valueOf(information[1])));
                sendService.sendSubscriberToRouters(information);
            }
        }
    }
}