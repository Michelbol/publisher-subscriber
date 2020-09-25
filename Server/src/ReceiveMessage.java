import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ReceiveMessage extends Thread {

    private Socket socket;

    ReceiveMessage(Socket socket){
        this.socket = socket;
        this.start();
    }

    public void run() {
        try{
            while(!socket.isClosed()){
                DataInputStream in = new DataInputStream(this.socket.getInputStream());
                Message.resolveRouter(new Request(in.readUTF()), this.socket);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
