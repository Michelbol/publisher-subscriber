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
                Message.resolve(TCPServer.splitMessage(in.readUTF()), this.socket);
                System.out.println("Receive message end");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
