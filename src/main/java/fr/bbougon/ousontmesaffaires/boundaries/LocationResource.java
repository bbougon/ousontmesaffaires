package fr.bbougon.ousontmesaffaires.boundaries;

import fr.bbougon.ousontmesaffaires.domain.location.Item;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.repositories.LocationRepository;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ItemJSON;
import fr.bbougon.ousontmesaffaires.web.helpers.Encoder;
import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path(LocationResource.PATH)
public class LocationResource {

    @Inject
    LocationRepository locationRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(final String payload) {
        Location location = new Location();
        location.add(Item.create(ItemJSON.from(payload)));
        locationRepository.persist(location);
        return Response.created(new URIBuilder().build(PATH + "/" + Encoder.toBase64(location.getId().toString()))).build();
    }

    static final String PATH = "/location";
}
