package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.command.CommandHandlers;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.command.location.ItemAddToLocationCommand;
import fr.bbougon.ousontmesaffaires.command.location.LocationAddCommand;
import fr.bbougon.ousontmesaffaires.web.helpers.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path(LocationResource.PATH)
public class LocationResource {

    @Inject
    CommandHandlers commandHandlers;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(final String payload) {
        Pair<UUID, Object> pair = commandHandlers.locationAdd().execute(new LocationAddCommand(payload));
        return Response.created(new URIBuilder().build(PATH + "/" + Encoder.toBase64(pair.getKey().toString()))).build();
    }

    @POST
    @Path("/{UUID}/item")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(@PathParam("UUID") final String locationId, final String payload) {
        Pair<Nothing, Object> pair = commandHandlers.itemAddToLocation().execute(new ItemAddToLocationCommand(UUID.fromString(Decoder.fromBase64(locationId)), payload));
        if (pair.getRight() == null) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    static final String PATH = "/location";
}
