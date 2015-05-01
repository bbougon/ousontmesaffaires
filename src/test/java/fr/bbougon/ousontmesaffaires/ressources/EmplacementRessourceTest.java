package fr.bbougon.ousontmesaffaires.ressources;

import fr.bbougon.ousontmesaffaires.entrepot.memoire.AvecEntrepotsMemoire;
import fr.bbougon.ousontmesaffaires.web.ressources.EmplacementRessource;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.CREATED;
import static org.fest.assertions.api.Assertions.assertThat;

public class EmplacementRessourceTest {

    @Rule
    public AvecEntrepotsMemoire entrepotsMemoire = new AvecEntrepotsMemoire();

    @Test
    public void peutCreerUnEmplacement() {
        Response ressource = new EmplacementRessource().creer();

        assertThat(ressource.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(ressource.getLocation().getPath()).matches("^/emplacement/[a-zA-Z0-9]{48}");
    }
}
