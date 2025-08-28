package chainofresp.payments.paymentTypes.onSite;

import chainofresp.payments.processor.PaymentProcessor;
import dto.request.OrderPaymentRqDto;
import model.Order;
import model.Receipt;

public class CashPayment implements PaymentProcessor {

    @Override
    public Receipt process(Order order, OrderPaymentRqDto rq) {
        return new Receipt(order.getId(), "PAYMENT_CHARGED");
    }
}
