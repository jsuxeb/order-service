package exceptions;

public class OrdersNotFoundException extends RuntimeException{
    public OrdersNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
