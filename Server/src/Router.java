import java.net.Socket;
import java.util.List;

public class Router {
    private RouterEnum routerEnum;
    private String interest;
    private Socket socket;

    public Router(RouterEnum routerEnum, String interest, Socket socket) {
        this.routerEnum = routerEnum;
        this.interest = interest;
        this.socket = socket;
    }

    public RouterEnum getRouterEnum() {
        return routerEnum;
    }

    public void setRouterEnum(RouterEnum routerEnum) {
        this.routerEnum = routerEnum;
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
