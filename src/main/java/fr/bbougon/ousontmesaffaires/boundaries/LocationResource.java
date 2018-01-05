package fr.bbougon.ousontmesaffaires.boundaries;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Article;
import fr.bbougon.ousontmesaffaires.domaine.emplacement.Location;
import fr.bbougon.ousontmesaffaires.entrepot.LocationRepository;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ArticleJSON;
import fr.bbougon.ousontmesaffaires.web.utilitaires.Encoder;
import fr.bbougon.ousontmesaffaires.web.utilitaires.URIBuilder;
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
        location.add(Article.create(ArticleJSON.from(payload)));
        locationRepository.persiste(location);
        return Response.created(new URIBuilder().build(PATH + "/" + Encoder.toBase64(location.getId().toString()))).build();
    }

    static final String PATH = "/location";
}
