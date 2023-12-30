package neko.transaction.commonbase.utils.exception;

public class ObjectStillUsingException extends RuntimeException {
    public ObjectStillUsingException(){

    }

    public ObjectStillUsingException(String message) {
        super(message);
    }
}
