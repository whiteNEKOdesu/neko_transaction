package neko.transaction.commonbase.utils.exception;

public class OrderPickException extends RuntimeException {
    public OrderPickException(){

    }

    public OrderPickException(String message) {
        super(message);
    }
}
