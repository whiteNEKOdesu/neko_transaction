package neko.transaction.commonbase.utils.exception;

public class ThirdPartyServiceException extends RuntimeException {
    public ThirdPartyServiceException(){

    }

    public ThirdPartyServiceException(String message) {
        super(message);
    }
}
