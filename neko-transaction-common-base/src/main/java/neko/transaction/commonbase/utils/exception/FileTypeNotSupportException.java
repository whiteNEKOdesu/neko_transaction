package neko.transaction.commonbase.utils.exception;

public class FileTypeNotSupportException extends RuntimeException {
    public FileTypeNotSupportException(){

    }

    public FileTypeNotSupportException(String message) {
        super(message);
    }
}
