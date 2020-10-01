package Models;

import Enums.RouterEnum;

import java.net.Socket;

public class RouterConnection {
    private RouterEnum routerEnum;
    private Socket socket;

    public RouterConnection(RouterEnum routerEnum, Socket socket) {
        this.routerEnum = routerEnum;
        this.socket = socket;
    }

    public RouterEnum getRouterEnum() {
        return routerEnum;
    }

    public void setRouterEnum(RouterEnum routerEnum) {
        this.routerEnum = routerEnum;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
