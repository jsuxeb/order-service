package repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Parameters;
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
    public Uni<List<Order>> findOrdersByUserId(String userId, int page, int pageSize) {
        if (page > 0) {
            page = page - 1;
        }
        return find("userId", userId)
                .page(page, pageSize)
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

        String jpql =
                "update Order o " +
                        "set o.status        = :status, " +
                        "    o.updatedAt     = :updatedAt, " +   // ← coma
                        "    o.intent        = :intent, " +      // ← coma
                        "    o.paymentType   = :paymentType, " + // ← coma
                        "    o.provider      = :provider, " +    // ← coma
                        "    o.paymentStatus = :paymentStatus " +
                        "where o.id = :id";

        return update(jpql, Parameters.with("status", order.getStatus())
                .and("updatedAt", order.getUpdatedAt())
                .and("intent", order.getIntent() == null ? null : order.getIntent())
                .and("paymentType", order.getPaymentType())
                .and("provider", order.getProvider())
                .and("paymentStatus", order.getPaymentStatus())
                .and("id", order.getId()))
                .invoke(rows -> log.info("Order updated. rows={}, id={}", rows, order.getId()))
                .replaceWith(order);

    }
}
