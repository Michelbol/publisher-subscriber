package Subscriber;

import Enums.ClientType;
import Enums.Operation;

public class Request {
    private ClientType type;
    private String interest;
    private String message;
    private String from;
    private String to;
    private Operation operation;

    public Request(ClientType type, String interest, String message, String from, String to, Operation operation) {
        this.type = type;
        this.interest = interest;
        this.message = message;
        this.from = from;
        this.to = to;
        this.operation = operation;
    }

    public Request(String request){
        String[] requestArray = request.split("\\|");

        this.type = ClientType.valueOf(requestArray[0]);
        this.to = requestArray[1];
        this.from = requestArray[2];
        this.interest = requestArray[3];
        this.message = requestArray[4];
        this.operation = Operation.valueOf(requestArray[5]);
    }

    static public String send(ClientType type, String interest, String message, String from, String to, Operation operation){
        return type.name()+"|"+to+"|"+from+"|"+interest+"|"+message+"|"+operation.name();
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

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
