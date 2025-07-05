package util;

import dto.OrderDto;
import dto.request.OrderRequestDto;
import model.Order;
import model.OrderItem;
import model.OrderStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectMapperUtil {
    public static Order convertOrderRequestDtoToOrder(OrderRequestDto orderRequestDto) {
        if (orderRequestDto == null) {
            return null;
        }
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setUserId(orderRequestDto.getUserId());
        if (orderRequestDto.getItems() != null) {
            List<OrderItem> items = orderRequestDto.getItems().stream().map(itemDto -> {
                OrderItem item = new OrderItem();
                item.setSku(itemDto.getSku());
                item.setQuantity(itemDto.getQuantity());
                item.setSubtotal(itemDto.getPrice() * item.getQuantity());
                item.setProductName(itemDto.getProductName());
                item.setUnitPrice(itemDto.getPrice());
                item.setProductType(itemDto.getItemType());
                item.setOrder(order);
                return item;
            }).collect(Collectors.toList());
            order.setItems(items);
        }
        Double totalAmount = order.getItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();

        order.setTotalAmount(totalAmount);
        return order;
    }

    public static String formatDate(LocalDateTime orderDate) {
        if (orderDate == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = orderDate.format(formatter);
        return formattedDate;
    }

    public static OrderDto convertOrderToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getId());
        orderDto.setUserId(order.getUserId().intValue());
        orderDto.setStatus(order.getStatus());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setCreatedAt(ObjectMapperUtil.formatDate(order.getCreatedAt()));
        if (order.getUpdatedAt() != null) {
            orderDto.setUpdatedAt(ObjectMapperUtil.formatDate(order.getUpdatedAt()));
        }
        return orderDto;
    }
}
