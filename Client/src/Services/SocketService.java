package Services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketService {

    private Socket socket;

    public void startSocket(Integer port) throws IOException {
        this.socket = new Socket("localhost", port);
    }

    public void send(String message) throws IOException {
        DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
        out.writeUTF(message);
    }

    public String receive() throws IOException {
        DataInputStream in = new DataInputStream(this.socket.getInputStream());
        return in.readUTF();
    }

    public boolean isClosed(){
        return socket.isClosed();
    }
}
