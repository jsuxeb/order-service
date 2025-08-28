package dto.request;

import model.PaymentIntent;

public class CardPaymentRqDto {
    private PaymentIntent intent; // CAPTURE o AUTHORIZE
    private String token;         // token simple de pago


    public PaymentIntent getIntent() {
        return intent;
    }

    public void setIntent(PaymentIntent intent) {
        this.intent = intent;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
