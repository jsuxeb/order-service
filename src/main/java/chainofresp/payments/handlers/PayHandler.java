package chainofresp.payments.handlers;

import chainofresp.payments.context.PaymentContext;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PayHandler {

    private static final Logger log = LoggerFactory.getLogger(PayHandler.class);
    private PayHandler next;

    public PayHandler linkWith(PayHandler next) {
        this.next = next;
        return next;
    }

    public Uni<PaymentContext> handle(PaymentContext ctx) {
        log.info("Iniciando handle");
        return doHandle(ctx)
                .chain(() -> next == null ? Uni.createFrom().item(ctx) : next.handle(ctx));
    }

    protected abstract Uni<Void> doHandle(PaymentContext ctx);


}
