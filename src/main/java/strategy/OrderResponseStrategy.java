package strategy;

import dto.OrderResponse;
import io.smallrye.mutiny.Uni;
import model.Order;


public interface OrderResponseStrategy {
    Uni<OrderResponse> buildResponse(Order order);
}
