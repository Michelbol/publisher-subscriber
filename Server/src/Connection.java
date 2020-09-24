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
                Message.resolve(TCPServer.splitMessage(data), this.socket);
                out.writeUTF("RESPONSE|"+data);
            }
        } catch(EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        } catch(IOException e) {
            System.out.println("IO:"+e.getMessage());
        }
    }
}