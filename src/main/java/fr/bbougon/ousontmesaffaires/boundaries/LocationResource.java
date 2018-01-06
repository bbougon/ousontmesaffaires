package fr.bbougon.ousontmesaffaires.boundaries;

import fr.bbougon.ousontmesaffaires.domain.location.*;
import fr.bbougon.ousontmesaffaires.repositories.LocationRepository;
import fr.bbougon.ousontmesaffaires.web.helpers.*;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@RequestScoped
@Path(LocationResource.PATH)
public class LocationResource {

    @Inject
    LocationRepository locationRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(final String payload) {
        Location location = new Location();
        location.add(Item.create(Features.getFeaturesFromPayload(payload)));
        locationRepository.persist(location);
        return Response.created(new URIBuilder().build(PATH + "/" + Encoder.toBase64(location.getId().toString()))).build();
    }

    @POST
    @Path("/{UUID}/item")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(@PathParam("UUID") final String locationId, final String payload) {
        Location location = locationRepository.findById(UUID.fromString(Decoder.fromBase64(locationId)));
        if(location == null){
            return Response.status(NOT_FOUND).build();
        }
        location.add(Item.create(Features.getFeaturesFromPayload(payload)));
        return Response.noContent().build();
    }

    static final String PATH = "/location";
}
