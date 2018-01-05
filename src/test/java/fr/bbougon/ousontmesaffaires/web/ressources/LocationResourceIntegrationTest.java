package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.entrepot.MongoConfiguration;
import fr.bbougon.ousontmesaffaires.rules.AvecServeurEmbarque;
import fr.bbougon.ousontmesaffaires.test.utils.JsonFileUtils;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.CREATED;
import static org.fest.assertions.api.Assertions.assertThat;

public class LocationResourceIntegrationTest {

    @Rule
    public AvecServeurEmbarque serveurEmbarque = new AvecServeurEmbarque();

    @Test
    public void canAddLocation() {
        String payload = new JsonFileUtils("json/article.json").getPayload();

        Response response = ClientBuilder.newClient().target(serveurEmbarque.getUrl())
                .path("/location")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(payload), Response.class);
        MongoConfiguration.stopSession();

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
    }

}