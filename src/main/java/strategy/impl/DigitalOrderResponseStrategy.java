package strategy.impl;

import dto.OrderResponse;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import model.Order;
import strategy.OrderResponseStrategy;
import util.ObjectMapperUtil;


@ApplicationScoped
public final class DigitalOrderResponseStrategy implements OrderResponseStrategy {


    @Override
    public Uni<OrderResponse> buildResponse(Order order) {
        return Uni.createFrom().item(ObjectMapperUtil.convertOrderToOrderDto(order));
    }
}
