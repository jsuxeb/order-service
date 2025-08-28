package chainofresp.payments.handlers;

import chainofresp.payments.context.PaymentContext;
import dto.request.CardPaymentRqDto;
//import dto.request.PaypalPaymentRqDto;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateHandler extends PayHandler {

    public ValidateHandler() {
        log.info("ValidateHandler Creado");
    }


    private static final Logger log = LoggerFactory.getLogger(ValidateHandler.class);

    @Override
    protected Uni<Void> doHandle(PaymentContext c) {
        log.info("Iniciando ValidateHandler");
        return Uni.createFrom().deferred(() -> {
            var rq = c.getRq();
            if (rq == null) {
                return Uni.createFrom().failure(new IllegalArgumentException("Request nulo"));
            }
            if (rq.getOrderId() == null) {
                return Uni.createFrom().failure(new IllegalArgumentException("orderId requerido"));
            }
            if (rq.getMethod() == null) {
                return Uni.createFrom().failure(new IllegalArgumentException("method requerido"));
            }

            // Validaciones específicas por método de pago
            switch (rq.getMethod()) {
                case VISA -> {
                    // Exigir DTO correcto si aplica
                  /*  if (!(rq instanceof CardPaymentRqDto)) {
                        log.info("AQUI IMPLEMENTAR validaciones para VISA");*/

                       /* return Uni.createFrom().failure(
                                new IllegalArgumentException("VISA requiere CardPaymentRqDto")
                        );*/
                    //}
                }
                case PAYPAL -> {
                    /*if (!(rq instanceof PaypalPaymentRqDto)) {
                        log.info("AQUI IMPLEMENTAR validaciones para VISA");
                        *//*return Uni.createFrom().failure(
                                new IllegalArgumentException("PAYPAL requiere PaypalPaymentRqDto")
                        );*//*
                    }*/
                }
                case CASH , CASH_ON_STORE-> {
                    // Si quieres validar un DTO específico para cash, hazlo aquí
                    // if (!(rq instanceof CashPaymentRqDto)) { ...fail... }
                }

                default -> {
                    return Uni.createFrom().failure(
                            new IllegalArgumentException("Método no soportado: " + rq.getMethod())
                    );
                }
            }

            return Uni.createFrom().voidItem(); // ✅ fin exitoso
        });
    }
}
