package neko.transaction.commonbase.utils.exception;

public class CodeIllegalException extends RuntimeException {
    public CodeIllegalException(){

    }

    public CodeIllegalException(String message) {
        super(message);
    }
}
