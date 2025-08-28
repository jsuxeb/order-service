package chainofresp.payments.handlers;

import chainofresp.payments.context.PaymentContext;
import dto.request.OrderPaymentRqDto;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import model.Order;
import model.PaymentIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.OrderRepository;

import java.time.LocalDateTime;

@ApplicationScoped
public class BuildOrderHandler extends PayHandler {


    private static final Logger log = LoggerFactory.getLogger(BuildOrderHandler.class);
    OrderRepository orderRepository;


    public BuildOrderHandler(OrderRepository orderRepository) {
        log.info("BuildOrderHandler Creado");
        this.orderRepository = orderRepository;
    }

    @Override
    protected Uni<Void> doHandle(PaymentContext ctx) {
        log.info("Iniciando BuildOrderHandler");
        var rq = ctx.getRq();
        if (rq == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Request nulo"));
        }

        return orderRepository.findOrderByOrderId(rq.getOrderId())
                .onItem().ifNull().failWith(() ->
                        new IllegalArgumentException("Pedido no encontrado con ID: " + rq.getOrderId()))
                .invoke(order -> {
                    updateOrder(order, rq, ctx);
                    ctx.setOrder(order);
                })
                .replaceWithVoid();
    }


    private void updateOrder(Order order, OrderPaymentRqDto rq, PaymentContext ctx) {
        order.setPaymentType(rq.getPaymentType());
        order.setProvider(rq.getMethod().name());
        order.setUpdatedAt(LocalDateTime.now());
        if (rq.getPaymentType().equalsIgnoreCase("card")) {
            order.setIntent(rq.getCardPaymentRqDto().getIntent());
        } else {
            order.setIntent(PaymentIntent.CAPTURE);
        }

    }
}
