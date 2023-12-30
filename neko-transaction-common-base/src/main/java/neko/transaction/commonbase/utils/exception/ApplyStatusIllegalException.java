package neko.transaction.commonbase.utils.exception;

public class ApplyStatusIllegalException extends RuntimeException {
    public ApplyStatusIllegalException(){

    }

    public ApplyStatusIllegalException(String message) {
        super(message);
    }
}
