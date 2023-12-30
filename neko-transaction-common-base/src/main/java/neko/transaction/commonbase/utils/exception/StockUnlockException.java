package neko.transaction.commonbase.utils.exception;

public class StockUnlockException extends RuntimeException {
    public StockUnlockException(){

    }

    public StockUnlockException(String message) {
        super(message);
    }
}
