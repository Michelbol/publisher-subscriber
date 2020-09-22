import java.util.List;

enum RouterEnum {
    I1(8000, new RouterEnum[]{}),
    I2(8001, new RouterEnum[]{I1}),
    I3(8002, new RouterEnum[]{}),
    I4(8003, new RouterEnum[]{I1,I3});

    public int routerPort;
    public RouterEnum[] linkRouters;

    RouterEnum(int routerPort, RouterEnum[] linkRouters) {
        this.routerPort = routerPort;
        this.linkRouters = linkRouters;
    }
}