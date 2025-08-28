package chainofresp.payments.paymentTypes.online;

//import dto.request.PaypalPaymentRqDto;
import model.Order;
import model.PaymentIntent;

public class PaypalPayment extends CardPaymentTemplate {

    @Override
    protected String authorize(Order o) {
        return "VISA-" + System.currentTimeMillis();
    }

    @Override
    protected void capture(Order o, String authId) {
        System.out.println("[VISA] Capturando " + o.getTotalAmount() + " con authId=" + authId);
        o.setIntent(PaymentIntent.CAPTURE);
    }
}
