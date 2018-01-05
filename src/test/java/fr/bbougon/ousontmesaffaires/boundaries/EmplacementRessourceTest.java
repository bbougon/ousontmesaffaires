package fr.bbougon.ousontmesaffaires.boundaries;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Emplacement;
import fr.bbougon.ousontmesaffaires.entrepot.memoire.EntrepotEmplacementMemoire;
import fr.bbougon.ousontmesaffaires.test.utils.JsonFileUtils;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

import static javax.ws.rs.core.Response.Status.CREATED;
import static org.fest.assertions.api.Assertions.assertThat;

public class EmplacementRessourceTest {

    @Test
    public void peutCreerUnEmplacement() throws IOException {
        EmplacementRessource emplacementRessource = new EmplacementRessource();
        emplacementRessource.entrepotEmplacement = new EntrepotEmplacementMemoire();

        Response ressource = emplacementRessource.creer(new JsonFileUtils("json/article.json").getPayload());

        assertThat(ressource.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(ressource.getLocation().getPath()).matches("^/emplacement/[a-zA-Z0-9]{48}");
        List<Emplacement> emplacements = emplacementRessource.entrepotEmplacement.getAll();
        assertThat(emplacements).isNotNull();
        assertThat(emplacements.get(0).getArticles()).isNotEmpty();
        assertThat(emplacements.get(0).getArticles().get(0).getContent()).isEqualTo("\"{\\\"type\\\":\\\"tshirt\\\",\\\"couleur\\\":\\\"blanc\\\",\\\"taille\\\":\\\"3ans\\\"}\"");
    }

}
