package Subscriber;

class Request {
    private ClientType type;
    private String interest;
    private String message;
    private String from;
    private String to;

    public Request(ClientType type, String interest, String message, String from, String to) {
        this.type = type;
        this.interest = interest;
        this.message = message;
        this.from = from;
        this.to = to;
    }

    public Request(String request){
        String[] requestArray = request.split("\\|");

        this.type = ClientType.valueOf(requestArray[0]);
        this.to = requestArray[1];;
        this.from = requestArray[2];;
        this.interest = requestArray[3];
        this.message = requestArray[4];;
    }

    static public String send(ClientType type, String interest, String message, String from, String to){
        return type.name()+"|"+to+"|"+from+"|"+interest+"|"+message;
    }

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
