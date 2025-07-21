package strategy.impl;


import client.UserClient;
import dto.OrderResponse;
import exceptions.ServiceExternalException;
import exceptions.UserNotFoundException;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import model.Order;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import strategy.OrderResponseStrategy;
import util.ObjectMapperUtil;

@ApplicationScoped
public final class POSOrderResponseStrategy implements OrderResponseStrategy {


    private static final Logger log = LoggerFactory.getLogger(POSOrderResponseStrategy.class);

    @RestClient
    UserClient userClient;

    @Override
    public Uni<OrderResponse> buildResponse(Order order) {
        return userClient.findUserByUserId(order.getUserId())
                .onFailure(WebApplicationException.class)
                .transform(Unchecked.function(t -> {
                    if (t.getMessage().contains("404")) {
                        return new UserNotFoundException("Usuario no encontrado con ID: " + order.getUserId());
                    }
                    return new ServiceExternalException("Error al consultar servicio de Cliente: " + order.getUserId());
                }))
                .onItem()
                .ifNull()
                .failWith(new UserNotFoundException("Usuario no encontrado con ID: " + order.getUserId()))
                .onItem().ifNotNull()
                .transformToUni(r -> Uni.createFrom().item(ObjectMapperUtil.convertToTicket(r, order)));

    }
}
