package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.command.container.ContainerAddCommand;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkTransaction;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;
import org.apache.commons.lang3.Validate;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/containers")
public class ContainerAddResource {

    @MongolinkTransaction
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(final String payload) {
        checkPayload(payload);
        CommandResponse commandResponse = commandBus.send(new ContainerAddCommand(payload));
        URI uri = new URIBuilder().build("/containers" + "/" + codec.urlSafeToBase64(commandResponse.getResponse().toString()));
        if (uri == null) {
            return Response.serverError().build();
        }
        return Response.created(uri).build();
    }

    static void checkPayload(final String payload) {
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
