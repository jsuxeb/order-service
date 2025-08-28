package facade;

import dto.OrderDto;
import dto.OrderResponse;
import dto.request.OrderPaymentRqDto;
import dto.request.OrderRequestDto;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import model.Order;
import model.OrderStatus;

import java.util.List;

public interface IOrderFacade {

    Uni<OrderResponse> creteOrder(String channel, @Valid OrderRequestDto request);

    Uni<List<OrderDto>> findOrdersByUserId(String userId, int page, int pageSize);

    Uni<OrderDto> updateOrder(Long orderId, OrderStatus status);

    Uni<OrderResponse> payOrder(OrderPaymentRqDto orderPaymentRqDto, String channel);

}
