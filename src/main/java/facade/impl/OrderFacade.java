package facade.impl;

import dto.OrderDto;
import dto.OrderResponse;
import dto.request.OrderPaymentRqDto;
import dto.request.OrderRequestDto;
import facade.IOrderFacade;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import model.Order;
import model.OrderStatus;
import service.MessagePublisherService;
import service.OrderService;
import util.ObjectMapperUtil;

import java.util.List;

@ApplicationScoped
public class OrderFacade implements IOrderFacade {


    @Inject
    OrderService orderService;

    @Inject
    MessagePublisherService messagePublisherService;

    @Override
    public Uni<OrderResponse> creteOrder(String channel, @Valid OrderRequestDto request) {
        return orderService.createOrder(request)
                .onItem()
                .ifNotNull()
                .transformToUni(this::sendEvents)
                .onItem().transform(ObjectMapperUtil::convertOrderToOrderDto);

    }

    @Override
    public Uni<List<OrderDto>> findOrdersByUserId(String userId, int page, int pageSize) {
        return orderService.findOrdersByUserId(userId, page, pageSize);
    }

    @Override
    public Uni<OrderDto> updateOrder(Long orderId, OrderStatus status) {
        return orderService.updateOrder(orderId, status)
                .onItem()
                .ifNotNull()
                .transformToUni(oderUpdated -> sendEvents(oderUpdated)
                        .onItem().transform(ObjectMapperUtil::convertOrderToOrderDto));
    }

    @Override
    public Uni<OrderResponse> payOrder(OrderPaymentRqDto orderPaymentRqDto, String channel) {
        return orderService.payOrder( orderPaymentRqDto)
                .onItem()
                .ifNotNull()
                .transformToUni(this::sendEvents)
                .onItem().transformToUni(r -> orderService.transformOrderResponse(r, channel));
    }

    private Uni<Order> sendEvents(Order Order) {
        Uni<Void> sendEvent = messagePublisherService.sendOrderToOrchestrator(Order);
        return sendEvent.flatMap(rs -> Uni.createFrom().item(Order));
    }

}
