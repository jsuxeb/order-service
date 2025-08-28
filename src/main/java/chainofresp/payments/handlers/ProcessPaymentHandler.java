package chainofresp.payments.handlers;

import chainofresp.payments.context.PaymentContext;
import io.smallrye.mutiny.Uni;
import model.OrderStatus;
import model.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessPaymentHandler extends PayHandler {

    private static final Logger log = LoggerFactory.getLogger(ProcessPaymentHandler.class);

    @Override
    protected Uni<Void> doHandle(PaymentContext ctx) {

        Receipt response = ctx.getProcessor()
                .process(ctx.getOrder(), ctx.getRq());
        ctx.setReceipt(response);
        ctx.getOrder().setPaymentStatus(response.getStatus());
        ctx.getOrder().setStatus(OrderStatus.ORDER_CONFIRMED);
        return Uni.createFrom().voidItem();
    }
}
