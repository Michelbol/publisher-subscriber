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
                Request request = new Request(data);

                if(request.getMessage().equals("RESPONSE")){
                    return;
                }

                if(request.getType().name().equals(ClientType.ROUTER.name())) {
                    Message.resolveRouter(request, this.socket);
                    out.writeUTF(Request.send(ClientType.ROUTER,request.getInterest(),request.getMessage(),request.getTo(),request.getFrom()));
                    return;
                }

                Message.resolveClient(request, this.socket);
                out.writeUTF(Request.send(request.getType(),request.getInterest(),request.getMessage(),request.getTo(),request.getFrom()));
            }
        } catch(EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        } catch(IOException e) {
            System.out.println("IO:"+e.getMessage());
        }
    }
}