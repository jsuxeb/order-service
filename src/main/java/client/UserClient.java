package client;

import dto.UserDto;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@Path("/users")
@RegisterRestClient(configKey = "user-host-client")
public interface UserClient {

    @GET
    @Path("{id}")
    @Consumes("application/json")
    Uni<UserDto> findUserByUserId(@PathParam("id") Long id);

}
