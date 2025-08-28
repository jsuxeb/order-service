package chainofresp.payments.processor;

import dto.request.OrderPaymentRqDto;
import model.Order;
import model.Receipt;

public interface PaymentProcessor {
    Receipt process(Order order, OrderPaymentRqDto rq);
}
