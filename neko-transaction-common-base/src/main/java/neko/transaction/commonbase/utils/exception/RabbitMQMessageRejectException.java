package neko.transaction.commonbase.utils.exception;

public class RabbitMQMessageRejectException extends RuntimeException {
    public RabbitMQMessageRejectException(){

    }

    public RabbitMQMessageRejectException(String message) {
        super(message);
    }
}
