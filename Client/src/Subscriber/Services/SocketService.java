package Subscriber.Services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketService {

    private Socket socket;

    public void startSocket() throws IOException {
        this.socket = new Socket("localhost", 7896);
    }

    public String send(String message) throws IOException {
        DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
        out.writeUTF(message);
        DataInputStream in = new DataInputStream(this.socket.getInputStream());
        return in.readUTF();
    }
}
