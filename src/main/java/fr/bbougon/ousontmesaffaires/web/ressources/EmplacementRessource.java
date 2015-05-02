package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Emplacement;
import fr.bbougon.ousontmesaffaires.entrepot.Entrepots;
import fr.bbougon.ousontmesaffaires.web.utilitaires.Encoder;
import fr.bbougon.ousontmesaffaires.web.utilitaires.URIBuilder;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(EmplacementRessource.PATH)
public class EmplacementRessource {

    @POST
    public Response creer() {
        Emplacement emplacement = new Emplacement();
        Entrepots.emplacement().persiste(emplacement);
        return Response.created(new URIBuilder().build(PATH + "/" + Encoder.toBase64(emplacement.getId().toString()))).build();
    }

    public static final String PATH = "/emplacement";
}
