package service.impl;

import dto.OrderDto;
import dto.request.OrderRequestDto;
import exceptions.OrdersNotFoundException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import model.Order;
import model.OrderStatus;
import repository.OrderRepository;
import service.MessagePublisherService;
import service.OrderService;
import util.ObjectMapperUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {

    @Inject
    OrderRepository orderRepository;

    @Inject
    MessagePublisherService messagePublisherService;

    @Override
    public Uni<OrderDto> createOrder(OrderRequestDto orderRequestDto) {
        Order order = ObjectMapperUtil.convertOrderRequestDtoToOrder(orderRequestDto);
        return orderRepository.saveOrder(order)
                .onFailure()
                .retry().atMost(3)
                .onItem()
                .ifNotNull()
                .transformToUni(this::sendEvents)
                .onItem().transform(ObjectMapperUtil::convertOrderToOrderDto);
    }

    private Uni<Order> sendEvents(Order Order) {
        Uni<Void> sendEvent = messagePublisherService.sendOrderToOrchestrator(Order);
        return sendEvent.flatMap(rs -> Uni.createFrom().item(Order));
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
        return orderRepository.findOrderByOrderId(orderId)
                .onItem().ifNull().failWith(() -> new OrdersNotFoundException("No se encontró el pedido con ID"))
                .onItem().ifNotNull()
                .transformToUni(order -> {
                    order.setStatus(status);
                    order.setUpdatedAt(LocalDateTime.now(ZoneId.of("America/Lima")));
                    return orderRepository.updateOrder(order);
                })
                .onItem()
                .ifNotNull()
                .transformToUni(oderUpdated -> sendEvents(oderUpdated)
                        .onItem().transform(ObjectMapperUtil::convertOrderToOrderDto));


    }


}
