package neko.transaction.commonbase.utils.exception;

public class EMailAlreadyExistException extends RuntimeException {
    public EMailAlreadyExistException(){

    }

    public EMailAlreadyExistException(String message) {
        super(message);
    }
}
