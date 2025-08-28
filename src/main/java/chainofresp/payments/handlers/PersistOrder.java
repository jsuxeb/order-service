package chainofresp.payments.handlers;

import chainofresp.payments.context.PaymentContext;
import io.smallrye.mutiny.Uni;
import repository.OrderRepository;

public class PersistOrder extends PayHandler {

    OrderRepository orderRepository;

    public PersistOrder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    protected Uni<Void> doHandle(PaymentContext ctx) {
        return orderRepository.updateOrder(ctx.getOrder())
                .onItem()
                .transformToUni(r -> Uni.createFrom().voidItem());
    }
}
