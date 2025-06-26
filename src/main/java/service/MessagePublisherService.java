package service;

import io.smallrye.mutiny.Uni;
import model.Order;

public interface MessagePublisherService {

    Uni<Void> sendOrderToOrchestrator(Order order);
}
