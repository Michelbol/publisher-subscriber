import java.net.Socket;

public class Subscriber {
    private String name;
    private String interest;
    private Socket socket;

    public Subscriber(String name, String interest, Socket socket) {
        this.name = name;
        this.interest = interest;
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
