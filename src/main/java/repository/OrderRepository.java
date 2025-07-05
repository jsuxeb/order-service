package repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {

    private static final Logger log = LoggerFactory.getLogger(OrderRepository.class);

    @WithTransaction
    public Uni<Order> saveOrder(Order order) {
        return persist(order);
    }

    @WithSession
    public Uni<List<Order>> findOrdersByUserId(String userId) {
        return find("userId", userId)
                .page(0, 10)
                .list();

    }

    @WithSession
    public Uni<Order> findOrderByOrderId(Long orderId) {
        return find("id", orderId)
                .firstResult()
                .onItem().ifNull()
                .failWith(new NotFoundException("No se encontró la orden con ID: " + orderId));

    }

    @WithTransaction
    public Uni<Order> updateOrder(Order order) {
        String query = "Update Order o set o.status = ?1, o.updatedAt = ?2 where o.id = ?3";
        return update(query, order.getStatus(), order.getUpdatedAt(), order.getId())
                .onItem().transform(updatedOrder -> {
                    log.info("Order updated successfully: {}", order.getId());
                    return updatedOrder;
                })
                .onFailure().invoke(throwable -> log.error("Error updating order: {}", throwable.getMessage()))
                .replaceWith(order);
    }
}
