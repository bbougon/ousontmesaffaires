package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.entrepot.MongoConfiguration;
import fr.bbougon.ousontmesaffaires.rules.AvecServeurEmbarque;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.CREATED;
import static org.fest.assertions.api.Assertions.assertThat;

public class EmplacementRessourceIntegrationTest {

    @Rule
    public AvecServeurEmbarque serveurEmbarque = new AvecServeurEmbarque();

    @Test
    public void peutCreerUnEmplacement() {
        Client client = ClientBuilder.newClient();

        Response response = client.target(serveurEmbarque.getUrl()).path("/emplacement").request().post(null);
        MongoConfiguration.stopSession();

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
    }

}