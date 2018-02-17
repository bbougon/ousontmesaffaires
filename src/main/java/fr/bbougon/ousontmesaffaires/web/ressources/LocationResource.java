package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.command.location.*;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

@Path(LocationResource.PATH)
public class LocationResource {

    @GET
    public Response getAll(@Context final UriInfo uriInfo) {
        CommandResponse commandResponse = commandBus.send(new LocationsGetCommand(codec, qrGenerator));
        return Response.ok().entity(commandResponse.getResponse()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(final String payload) {
        CommandResponse commandResponse = commandBus.send(new LocationAddCommand(payload));
        return Response.created(new URIBuilder().build(PATH + "/" + codec.urlSafeToBase64(commandResponse.getResponse().toString()))).build();
    }

    @POST
    @Path("/{UUID}/item")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(@PathParam("UUID") final String locationId, final String payload) {
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
        CommandResponse commandResponse = commandBus.send(new LocationGetCommand(codec, locationId, uriInfo, qrGenerator));
        return Response.status(OK).entity(commandResponse.getResponse()).build();
    }

    @Inject
    CommandBus commandBus;

    @Inject
    QRGenerator qrGenerator;

    @Inject
    Codec codec;

    static final String PATH = "/locations";
}
