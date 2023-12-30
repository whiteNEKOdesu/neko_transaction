package neko.transaction.commonbase.utils.exception;

public class OAuthException extends RuntimeException {
    public OAuthException(){

    }

    public OAuthException(String message) {
        super(message);
    }
}
