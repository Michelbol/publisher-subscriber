package Models;

import Enums.RouterEnum;
import java.net.Socket;

public class Router {
    private String interest;
    private Socket socket;
    private RouterEnum routerEnum;

    public Router(String interest, Socket socket, RouterEnum routerEnum) {
        this.interest = interest;
        this.socket = socket;
        this.routerEnum = routerEnum;
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
