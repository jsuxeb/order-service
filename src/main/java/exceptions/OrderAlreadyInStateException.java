package exceptions;

public class OrderAlreadyInStateException extends RuntimeException {
    public OrderAlreadyInStateException(String errorMessage) {
        super(errorMessage);
    }
}
