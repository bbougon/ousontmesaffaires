package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.command.container.ContainerPatchCommand;
import fr.bbougon.ousontmesaffaires.command.container.ItemAddToContainerCommand;
import fr.bbougon.ousontmesaffaires.command.container.ContainerAddCommand;
import fr.bbougon.ousontmesaffaires.command.container.ContainerGetCommand;
import fr.bbougon.ousontmesaffaires.command.container.ContainersGetCommand;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;
import fr.bbougon.ousontmesaffaires.web.ressources.javax.ws.rs.PATCH;
import org.apache.commons.lang3.Validate;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

@Path(ContainerResource.PATH)
public class ContainerResource {

    @GET
    public Response getAll(@Context UriInfo uriInfo) {
        CommandResponse commandResponse = commandBus.send(new ContainersGetCommand(qrGenerator, uriInfo));
        return Response.ok().entity(commandResponse.getResponse()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(final String payload) {
        checkPayload(payload);
        CommandResponse commandResponse = commandBus.send(new ContainerAddCommand(payload));
        URI uri = new URIBuilder().build(PATH + "/" + codec.urlSafeToBase64(commandResponse.getResponse().toString()));
        if(uri == null) {
            return Response.serverError().build();
        }
        return Response.created(uri).build();
    }

    @POST
    @Path("/{UUID}/item")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(@PathParam("UUID") final String containerId, final String payload) {
        checkPayload(payload);
        CommandResponse commandResponse = commandBus.send(new ItemAddToContainerCommand(UUID.fromString(codec.fromBase64(containerId)), payload));
        if (commandResponse.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{UUID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContainer(@Context final UriInfo uriInfo, @PathParam("UUID") final String containerId) {
        CommandResponse commandResponse = commandBus.send(new ContainerGetCommand(containerId, uriInfo, qrGenerator));
        return Response.status(OK).entity(commandResponse.getResponse()).build();
    }

    @PATCH
    @Path("/{UUID}")
    public Response patchContainer(@PathParam("UUID") final String containerId, final String patch) {
        commandBus.send(new ContainerPatchCommand(containerId, patch));
        return Response.status(OK).build();
    }

    private void checkPayload(final String payload) {
        Validate.notNull(payload, "Payload cannot be null.");
        Validate.notEmpty(payload.trim(), "Payload cannot be empty.");
    }

    @Inject
    CommandBus commandBus;

    @Inject
    QRGenerator qrGenerator;

    @Inject
    Codec codec;

    public static final String PATH = "/containers";
}
