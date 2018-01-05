package fr.bbougon.ousontmesaffaires.boundaries;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Article;
import fr.bbougon.ousontmesaffaires.domaine.emplacement.Emplacement;
import fr.bbougon.ousontmesaffaires.entrepot.EntrepotEmplacement;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ArticleJSON;
import fr.bbougon.ousontmesaffaires.web.utilitaires.Encoder;
import fr.bbougon.ousontmesaffaires.web.utilitaires.URIBuilder;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path(EmplacementRessource.PATH)
public class EmplacementRessource {

    @Inject
    EntrepotEmplacement entrepotEmplacement;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response creer(final String payload) {
        Emplacement emplacement = new Emplacement();
        emplacement.add(Article.create(ArticleJSON.from(payload)));
        entrepotEmplacement.persiste(emplacement);
        return Response.created(new URIBuilder().build(PATH + "/" + Encoder.toBase64(emplacement.getId().toString()))).build();
    }

    static final String PATH = "/emplacement";
}
