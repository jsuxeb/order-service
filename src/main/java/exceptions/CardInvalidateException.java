package exceptions;

public class CardInvalidateException extends RuntimeException {

    public CardInvalidateException(String errorMessage) {
        super(errorMessage);
    }
}
