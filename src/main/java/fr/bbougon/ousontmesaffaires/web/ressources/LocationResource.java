package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.command.CommandHandlers;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.command.location.*;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

@Path(LocationResource.PATH)
public class LocationResource {

    @Inject
    CommandHandlers commandHandlers;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(final String payload) {
        Pair<UUID, Object> pair = commandHandlers.locationAdd().execute(new LocationAddCommand(payload));
        return Response.created(new URIBuilder().build(PATH + "/" + codec.urlSafeToBase64(pair.getKey().toString()))).build();
    }

    @POST
    @Path("/{UUID}/item")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(@PathParam("UUID") final String locationId, final String payload) {
        Pair<Nothing, Object> pair = commandHandlers.itemAddToLocation().execute(new ItemAddToLocationCommand(UUID.fromString(codec.fromBase64(locationId)), payload));
        if (pair.getRight() == null) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{UUID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocation(@Context final UriInfo uriInfo, @PathParam("UUID") final String locationId) {
        Pair<String, Object> result = commandHandlers.locationGet().execute(new LocationGetCommand(UUID.fromString(codec.fromBase64(locationId)), uriInfo));
        return Response.status(OK).entity(result.getLeft()).build();
    }
    @Inject
    Codec codec;

    static final String PATH = "/location";
}
