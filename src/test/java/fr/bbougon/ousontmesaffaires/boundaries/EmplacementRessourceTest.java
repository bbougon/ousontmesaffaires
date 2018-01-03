package fr.bbougon.ousontmesaffaires.boundaries;

import fr.bbougon.ousontmesaffaires.entrepot.memoire.EntrepotEmplacementMemoire;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.CREATED;
import static org.fest.assertions.api.Assertions.assertThat;

public class EmplacementRessourceTest {

    @Test
    public void peutCreerUnEmplacement() {
        EmplacementRessource emplacementRessource = new EmplacementRessource();
        emplacementRessource.entrepotEmplacement = new EntrepotEmplacementMemoire();

        Response ressource = emplacementRessource.creer();

        assertThat(ressource.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(ressource.getLocation().getPath()).matches("^/emplacement/[a-zA-Z0-9]{48}");
    }
}
