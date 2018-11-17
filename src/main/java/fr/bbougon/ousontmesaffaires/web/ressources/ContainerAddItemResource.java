package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.saga.item.ContainerAddItemSaga;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkTransaction;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.apache.commons.lang3.Validate;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/containers")
public class ContainerAddItemResource {

    @MongolinkTransaction
    @POST
    @Path("/{UUID}/item")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(@PathParam("UUID") final String containerId, final String payload) {
        checkPayload(payload);
        CommandResponse commandResponse = commandBus.send(new ContainerAddItemSaga(UUID.fromString(codec.fromBase64(containerId)), payload));
        if (commandResponse.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    private void checkPayload(final String payload) {
        Validate.notNull(payload, "Payload cannot be null.");
        Validate.notEmpty(payload.trim(), "Payload cannot be empty.");
        if (payload.trim().replaceAll(" ", "").equals("{}")) {
            throw new IllegalArgumentException("Payload cannot be empty.");
        }
    }

    @Inject
    CommandBus commandBus;

    @Inject
    Codec codec;

}
