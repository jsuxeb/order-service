package util;

import dto.OrderDto;
import dto.request.OrderRequestDto;
import model.Order;
import model.OrderItem;
import model.OrderStatus;

import java.time.LocalDateTime;
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
        order.setOrderDate(LocalDateTime.now());
        order.setUserId(orderRequestDto.getUserId());
        if (orderRequestDto.getItems() != null) {
            List<OrderItem> items = orderRequestDto.getItems().stream().map(itemDto -> {
                OrderItem item = new OrderItem();
                item.setSku(itemDto.getSku());
                item.setQuantity(itemDto.getQuantity());
                item.setSubtotal(itemDto.getPrice() * item.getQuantity());
                item.setProductName(itemDto.getProductName());
                item.setUnitPrice(itemDto.getPrice());
                return item;

                /*    private String sku;
    private String productName;
    private int quantity;
    private double price;
    private double subtotal;
*/
            }).collect(Collectors.toList());
            order.setItems(items);
        }
        Double totalAmount = order.getItems().stream()
                .mapToDouble(item -> item.getSubtotal())
                .sum();

        order.setTotalAmount(totalAmount);
        return order;
    }

    public static String formatDate(LocalDateTime orderDate) {
        if (orderDate == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDate = orderDate.format(formatter);
        return formattedDate;
    }

    public static OrderDto convertOrderToOrderDto(Order orderItem) {
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(orderItem.getUserId());
        orderDto.setStatus(orderItem.getStatus());
        orderDto.setOrderDate(ObjectMapperUtil.formatDate(orderItem.getOrderDate()));
        orderDto.setTotalAmount(orderItem.getTotalAmount());
        return orderDto;
    }
}
