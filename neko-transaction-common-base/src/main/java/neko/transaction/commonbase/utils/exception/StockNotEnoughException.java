package neko.transaction.commonbase.utils.exception;

public class StockNotEnoughException extends RuntimeException {
    public StockNotEnoughException(){

    }

    public StockNotEnoughException(String message) {
        super(message);
    }
}
