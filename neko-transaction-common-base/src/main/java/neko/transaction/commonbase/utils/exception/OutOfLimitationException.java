package neko.transaction.commonbase.utils.exception;

public class OutOfLimitationException extends RuntimeException {
    public OutOfLimitationException(){

    }

    public OutOfLimitationException(String message) {
        super(message);
    }
}
