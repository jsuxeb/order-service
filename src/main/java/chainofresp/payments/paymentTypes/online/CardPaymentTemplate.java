package chainofresp.payments.paymentTypes.online;

import chainofresp.payments.processor.PaymentProcessor;
import constant.TokenMessage;
import dto.request.OrderPaymentRqDto;
import model.Order;
import model.PaymentIntent;
import model.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static constant.StatusPaymentMessage.PAYMENT_STATUS_PAID;
import static constant.StatusPaymentMessage.STATUS_PAYMENT_AUTHORIZED;
import static constant.TokenMessage.*;

public abstract class CardPaymentTemplate implements PaymentProcessor {

    private static final Logger log = LoggerFactory.getLogger(CardPaymentTemplate.class);

    @Override
    public Receipt process(Order order, OrderPaymentRqDto rq) {
        validate(rq);
        String authId = authorize(order);
        if (order.getIntent() == PaymentIntent.CAPTURE) {
            capture(order, authId);
            return new Receipt(order.getId(), PAYMENT_STATUS_PAID);
        }
        return new Receipt(order.getId(), STATUS_PAYMENT_AUTHORIZED);
    }

    protected void validate(OrderPaymentRqDto request) {

        String token = request.getCardPaymentRqDto().getToken();

        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException(TOKEN_EMPTY_ERROR);
        }

        // Validación ficticia: el token debe empezar con "tok_"
        if (!token.startsWith("tok_")) {
            throw new IllegalArgumentException(INVALID_TOKEN_FORMAT_MESSAGE);
        }

        // Validación ficticia: longitud mínima
        if (token.length() < 10) {
            throw new IllegalArgumentException(TOKEN_TOO_SHORT_MESSAGE);
        }

        // Validación ficticia: contener letras y números
        if (!token.matches(".*[a-zA-Z].*") || !token.matches(".*[0-9].*")) {
            throw new IllegalArgumentException(TOKEN_MUST_CONTAIN_LETTERS_AND_NUMBERS);
        }

        // Validación: lista negra de tokens no permitidos
        if ("tok_invalid".equals(token)) {
            throw new IllegalArgumentException(BLACKLISTED_TOKEN);
        }

    }

    protected abstract String authorize(Order o);

    protected abstract void capture(Order o, String authId);
}
