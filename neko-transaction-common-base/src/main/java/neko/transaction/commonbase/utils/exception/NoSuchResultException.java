package neko.transaction.commonbase.utils.exception;

public class NoSuchResultException extends RuntimeException {
    public NoSuchResultException(){

    }

    public NoSuchResultException(String message) {
        super(message);
    }
}
