package chainofresp.payments.processor;

import chainofresp.payments.context.PaymentContext;
import chainofresp.payments.handlers.PayHandler;
import factory.PayOrderFactory;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectProcessorHandler extends PayHandler {


    private static final Logger log = LoggerFactory.getLogger(SelectProcessorHandler.class);

    @Override
    protected Uni<Void> doHandle(PaymentContext c) {
        c.setProcessor(PayOrderFactory.create(c.getRq().getMethod()));
        return Uni.createFrom().voidItem();
    }
}
