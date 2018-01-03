package fr.bbougon.ousontmesaffaires.boundaries;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Emplacement;
import fr.bbougon.ousontmesaffaires.entrepot.EntrepotEmplacement;
import fr.bbougon.ousontmesaffaires.web.utilitaires.Encoder;
import fr.bbougon.ousontmesaffaires.web.utilitaires.URIBuilder;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@RequestScoped
@Path(EmplacementRessource.PATH)
public class EmplacementRessource {

    @Inject
    EntrepotEmplacement entrepotEmplacement;

    @POST
    public Response creer() {
        Emplacement emplacement = new Emplacement();
        entrepotEmplacement.persiste(emplacement);
        return Response.created(new URIBuilder().build(PATH + "/" + Encoder.toBase64(emplacement.getId().toString()))).build();
    }

    static final String PATH = "/emplacement";
}
