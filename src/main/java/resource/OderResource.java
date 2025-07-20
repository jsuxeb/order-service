package resource;

import dto.OrderDto;
import dto.OrderUpdateDto;
import dto.request.OrderRequestDto;
import exceptions.ErrorHandlerUtil;
import exceptions.OrderAlreadyInStateException;
import exceptions.OrdersNotFoundException;
import facade.OrderFacade;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.OrderService;

@Path("/api/v1/orders")
public class OderResource {

    @Inject
    OrderService orderService;

    @Inject
    OrderFacade orderFacade;

/*    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createOrder(@DefaultValue("DIGITAL") @QueryParam("channel") String channel, @Valid final OrderRequestDto order) {
        return orderService.createOrder(order)
                .onItem()
                .transform(orderDto -> Response.status(Response.Status.CREATED).entity(orderDto).build())
                .onFailure()
                .recoverWithItem(ex -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(ex.getMessage())
                        .build());

    }*/

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createOrder(@DefaultValue("DIGITAL") @QueryParam("channel") String channel, @Valid final OrderRequestDto order) {
        return orderFacade.processOrder(channel, order)
                .onItem()
                .transform(orderDto -> {
                    MediaType mediaType = channel.equalsIgnoreCase("POS")
                            ? MediaType.TEXT_PLAIN_TYPE
                            : MediaType.APPLICATION_JSON_TYPE;
                    return Response.status(Response.Status.CREATED).entity(orderDto).type(mediaType).build();
                })
                .onFailure()
                .recoverWithItem(ex -> Response.status(ErrorHandlerUtil.createErrorMessage(ex).getStatus())
                        .entity(ex.getMessage())
                        .build());

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> findOrderByUserId(@QueryParam("userId") String userId) {
        return orderService.findOrdersByUserId(userId)
                .onItem()
                .transform(list -> Response.ok(list).status(Response.Status.OK).build())
                .onFailure(OrdersNotFoundException.class)
                .recoverWithItem(e -> Response.status(Response.Status.NOT_FOUND)
                        .entity(ErrorHandlerUtil.createErrorMessage(e))
                        .build());
    }


    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateOrderStatus(OrderUpdateDto orderDto) {
        return orderService.updateOrder(orderDto.getOrderId(), orderDto.getStatus())
                .onItem()
                .transform(updatedOrder -> Response.ok(updatedOrder).status(Response.Status.OK).build())
                .onFailure(OrdersNotFoundException.class)
                .recoverWithItem(e -> Response.status(Response.Status.NOT_FOUND)
                        .entity(ErrorHandlerUtil.createErrorMessage(e))
                        .build())
                .onFailure(OrderAlreadyInStateException.class)
                .recoverWithItem(e -> Response.status(Response.Status.CONFLICT)
                        .entity(ErrorHandlerUtil.createErrorMessage(e))
                        .build());

    }
}
