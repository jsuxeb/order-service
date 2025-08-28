package chainofresp.payments.paymentTypes.online;

import model.Order;
import model.PaymentIntent;

public class VisaPayment extends CardPaymentTemplate {




    @Override
    protected String authorize(Order o) {
        System.out.println("[VISA] Autorizando " + o.getTotalAmount() + " para orden " + o.getId());
        return "VISA-" + System.currentTimeMillis();
    }

    @Override
    protected void capture(Order o, String authId) {
        System.out.println("[VISA] Capturando monto " + o.getTotalAmount() + " con authId=" + authId);
        o.setIntent(PaymentIntent.CAPTURE);
    }
}
