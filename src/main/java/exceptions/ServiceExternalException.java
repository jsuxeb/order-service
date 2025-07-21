package exceptions;

public class ServiceExternalException extends RuntimeException {
    public ServiceExternalException(String errorMessage) {
        super(errorMessage);
    }

}