package service;

import dto.OrderDto;
import dto.OrderResponse;
import dto.request.OrderRequestDto;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;
import model.Order;
import model.OrderStatus;

import java.util.List;

public interface OrderService {
    Uni<Order> createOrder(OrderRequestDto orderRequestDto);

    Uni<List<OrderDto>> findOrdersByUserId(String userId);

    Uni<OrderDto> findOrderByOrderId(Long orderId);

    Uni<Order> updateOrder(Long orderId, OrderStatus status);

    Uni<OrderResponse> transformOrderResponse(Order r, String channel);
}
