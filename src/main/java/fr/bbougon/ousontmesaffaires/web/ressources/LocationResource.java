package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.command.location.ItemAddToLocationCommand;
import fr.bbougon.ousontmesaffaires.command.location.LocationAddCommand;
import fr.bbougon.ousontmesaffaires.command.location.LocationGetCommand;
import fr.bbougon.ousontmesaffaires.command.location.LocationsGetCommand;
import fr.bbougon.ousontmesaffaires.command.sticker.GenerateStickersCommand;
import fr.bbougon.ousontmesaffaires.command.sticker.Sticker;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;
import org.apache.commons.lang3.Validate;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;

import static javax.ws.rs.core.HttpHeaders.CONTENT_DISPOSITION;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

@Path(LocationResource.PATH)
public class LocationResource {

    @GET
    public Response getAll(@Context UriInfo uriInfo) {
        CommandResponse commandResponse = commandBus.send(new LocationsGetCommand(qrGenerator, uriInfo));
        return Response.ok().entity(commandResponse.getResponse()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(final String payload) {
        checkPayload(payload);
        CommandResponse commandResponse = commandBus.send(new LocationAddCommand(payload));
        URI uri = new URIBuilder().build(PATH + "/" + codec.urlSafeToBase64(commandResponse.getResponse().toString()));
        if(uri == null) {
            return Response.serverError().build();
        }
        return Response.created(uri).build();
    }

    @POST
    @Path("/{UUID}/item")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(@PathParam("UUID") final String locationId, final String payload) {
        checkPayload(payload);
        CommandResponse commandResponse = commandBus.send(new ItemAddToLocationCommand(UUID.fromString(codec.fromBase64(locationId)), payload));
        if (commandResponse.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{UUID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocation(@Context final UriInfo uriInfo, @PathParam("UUID") final String locationId) {
        CommandResponse commandResponse = commandBus.send(new LocationGetCommand(locationId, uriInfo, qrGenerator));
        return Response.status(OK).entity(commandResponse.getResponse()).build();
    }

    @PUT
    @Path("/sticker/{UUID}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response generateStickers(@PathParam("UUID") final String locationId, final String payload) {
        CommandResponse<Sticker> commandResponse = commandBus.send(new GenerateStickersCommand(locationId, payload));
        if(commandResponse.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.ok(commandResponse.getResponse().getContent()).header(CONTENT_DISPOSITION, "filename=" + commandResponse.getResponse().getName()).build();
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

    public static final String PATH = "/locations";
}
