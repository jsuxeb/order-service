package service.impl;

import dto.OrderDto;
import dto.OrderResponse;
import dto.request.OrderRequestDto;
import exceptions.OrdersNotFoundException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import model.Order;
import model.OrderStatus;
import repository.OrderRepository;
import service.OrderService;
import strategy.ContextStrategies;
import util.ObjectMapperUtil;

import java.util.List;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {

    @Inject
    OrderRepository orderRepository;

    @Inject
    ContextStrategies contextStrategies;

    @Override
    public Uni<Order> createOrder(OrderRequestDto orderRequestDto) {
        Order order = ObjectMapperUtil.convertOrderRequestDtoToOrder(orderRequestDto);
        return orderRepository.saveOrder(order)
                .onFailure()
                .retry().atMost(3);

    }

    @Override
    public Uni<List<OrderDto>> findOrdersByUserId(String userId) {
        return orderRepository.findOrdersByUserId(userId)
                .onItem()
                .transform(orders -> {
                    if (orders == null || orders.isEmpty()) {
                        throw new OrdersNotFoundException("No existen pedidos para el usuario con ID: " + userId);
                    }
                    return orders.stream()
                            .map(ObjectMapperUtil::convertOrderToOrderDto)
                            .toList();
                });
    }

    @Override
    public Uni<OrderDto> findOrderByOrderId(Long orderId) {
        return orderRepository.findOrderByOrderId(orderId)
                .onItem()
                .ifNull()
                .failWith(() -> new OrdersNotFoundException("No se encontró el pedido con ID: " + orderId))
                .onItem().ifNotNull().transform(ObjectMapperUtil::convertOrderToOrderDto);
    }

    @Override
    public Uni<OrderDto> updateOrder(Long orderId, OrderStatus status) {
        /*return orderRepository.findOrderByOrderId(orderId)
                .onItem().ifNull().failWith(() -> new OrdersNotFoundException("No se encontró el pedido con ID: " + orderId))
                .onItem().ifNotNull()
                .transformToUni(order -> {
                    if (order.getStatus().name().equalsIgnoreCase(status.name())) {
                        return Uni.createFrom().failure(() -> new OrderAlreadyInStateException(
                                "El pedido con ID: " + orderId + " ya se encuentra en el estado: " + status.name()
                        ));
                    }
                    order.setStatus(status);
                    order.setUpdatedAt(LocalDateTime.now(ZoneId.of("America/Lima")));
                    return orderRepository.updateOrder(order);
                })
                .onItem()
                .ifNotNull()
                .transformToUni(oderUpdated -> sendEvents(oderUpdated)
                        .onItem().transform(ObjectMapperUtil::convertOrderToOrderDto));*/

        return Uni.createFrom().nullItem();
    }

    @Override
    public Uni<OrderResponse> transformOrderResponse(Order r, String channel) {
        return contextStrategies.getStrategyOrderResponse(channel).buildResponse(r);
    }
}
