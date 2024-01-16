package neko.transaction.commonbase.utils.exception;

public class ProductServiceException extends RuntimeException {
    public ProductServiceException(){

    }

    public ProductServiceException(String message) {
        super(message);
    }
}
