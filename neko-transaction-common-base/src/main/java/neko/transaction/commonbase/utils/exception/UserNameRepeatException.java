package neko.transaction.commonbase.utils.exception;

public class UserNameRepeatException extends RuntimeException {
    public UserNameRepeatException(){

    }

    public UserNameRepeatException(String message) {
        super(message);
    }
}
