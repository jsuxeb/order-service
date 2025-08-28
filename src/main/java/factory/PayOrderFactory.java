package factory;

import chainofresp.payments.processor.PaymentProcessor;
import chainofresp.payments.paymentTypes.onSite.CashPayment;
import model.PaymentMethod;
import chainofresp.payments.paymentTypes.online.PaypalPayment;
import chainofresp.payments.paymentTypes.online.VisaPayment;

public class PayOrderFactory {
    public static PaymentProcessor create(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("El método de pago no puede ser null");
        }

        return switch (method) {
            case VISA -> new VisaPayment();
            case PAYPAL -> new PaypalPayment();
            case CASH, CASH_ON_STORE-> new CashPayment();
            default     -> throw new IllegalArgumentException("Método de pago no soportado: " + method);

        };
    }
}
