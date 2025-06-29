package resource;

import dto.OrderDto;
import dto.OrderUpdateDto;
import dto.request.OrderRequestDto;
import exceptions.ErrorHandlerUtil;
import exceptions.OrdersNotFoundException;
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createOrder(@Valid final OrderRequestDto order) {
        return orderService.createOrder(order)
                .onItem()
                .transform(orderDto -> Response.status(Response.Status.CREATED).entity(orderDto).build())
                .onFailure()
                .recoverWithItem(ex -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
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
                        .build());

    }
}
