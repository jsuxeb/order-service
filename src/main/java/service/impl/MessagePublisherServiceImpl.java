package service.impl;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import model.Order;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.MessagePublisherService;

import java.time.ZoneOffset;
import java.util.UUID;

@ApplicationScoped
public class MessagePublisherServiceImpl implements MessagePublisherService {


    private static final Logger log = LoggerFactory.getLogger(MessagePublisherServiceImpl.class);

    @Inject
    @Channel("order-emitter")
    Emitter<avro.model.Order> emitter;

    @Override
    public Uni<Void> sendOrderToOrchestrator(Order order) {
        log.info("Sync stock event sending...");

        var message = Message.of(convertOrderToAvro(order))
                .addMetadata(OutgoingKafkaRecordMetadata.builder().withKey(UUID.randomUUID().toString()).build());

        emitter.send(message);
        return Uni.createFrom().voidItem();
    }

    private avro.model.Order convertOrderToAvro(Order order) {
        avro.model.Order orderAvro = new avro.model.Order(
                order.getId(),
                order.getUserId(),
                order.getOrderDate() != null ? order.getOrderDate().toInstant(ZoneOffset.UTC) : null,
                order.getItems() != null ? order.getItems().stream()
                        .map(this::convertOrderItemToAvro)
                        .toList() : null,
                order.getStatus() != null ? avro.model.OrderStatus.valueOf(order.getStatus().name()) : null,
                order.getTotalAmount()
        );

        return orderAvro;
    }



    private avro.model.OrderItem convertOrderItemToAvro(model.OrderItem item) {
        return new avro.model.OrderItem(
                item.getSku(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }


}
