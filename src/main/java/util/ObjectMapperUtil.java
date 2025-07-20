package util;

import dto.OrderDto;
import dto.OrderResponse;
import dto.UserDto;
import dto.request.OrderRequestDto;
import dto.response.TicketFormatterResponse;
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

    public static OrderResponse convertToTicket(UserDto userDto, Order order) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        sb.append("========================================\n");
        sb.append("             ORDER RECEIPT              \n");
        sb.append("       Company GALAXY STORE ® S.A.C.     \n");
        sb.append("========================================\n");
        sb.append(" Customer: ").append(userDto.getName()).append("\n");
        sb.append(" Date    : ").append(order.getCreatedAt().format(formatter)).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("             Products                   \n");
        sb.append("             ¯¯¯¯¯¯¯¯                   \n");
        List<OrderItem> items = order.getItems();
        for (OrderItem item : items) {
            sb.append(String.format(" %-22s %2dx S/ %6.2f\n",
                    trimToLength(item.getProductName(), 22), item.getQuantity(), item.getUnitPrice()));
        }

        sb.append("                ...                     \n"); // Indica que puede haber más ítems
        sb.append("----------------------------------------\n");
        sb.append(String.format(" TOTAL                     : S/ %6.2f\n", order.getTotalAmount()));
        sb.append("========================================\n");
        sb.append("        Thank you for your order!       \n");
        sb.append("========================================\n");
        TicketFormatterResponse ticketFormatterResponse = new TicketFormatterResponse();
        ticketFormatterResponse.setResponse(sb.toString());
        return ticketFormatterResponse;
    }

    private static String trimToLength(String text, int maxLength) {
        return text.length() <= maxLength ? text : text.substring(0, maxLength - 3) + "...";
    }
}
