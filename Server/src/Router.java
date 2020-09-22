import java.net.Socket;
import java.util.List;

public class Router {
    private String interest;
    private Socket socket;

    public Router(String interest, Socket socket) {
        this.interest = interest;
        this.socket = socket;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
