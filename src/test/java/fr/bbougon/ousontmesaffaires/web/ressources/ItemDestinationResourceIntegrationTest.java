package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.WithEmbeddedServer;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.test.utils.ClientUtilsForTests;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemDestinationResourceIntegrationTest {

    @Rule
    public WithEmbeddedServer withEmbeddedServer = new WithEmbeddedServer();

    @Test
    public void returns404OnUnexistingContainer() {
        String uuid = new Codec().urlSafeToBase64(UUID.randomUUID().toString());

        Response response = ClientBuilder.newClient()
                .target("http://localhost:17000")
                .path("containers")
                .path(uuid)
                .path("items")
                .path("abcde")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"destination\":\"" + uuid + "\"}"));

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
        assertThat(response.readEntity(String.class)).contains("The container you are requesting does not exist.");
    }

    @Test
    public void returns404OnUnexistingItem() {
        Response container = ClientUtilsForTests.createContainer("json/pantalon.json");
        Response existingContainer = ClientUtilsForTests.createContainer("json/t-shirt.json");

        Response response = ClientBuilder.newClient()
                .target("http://localhost:17000")
                .path("containers")
                .path(ClientUtilsForTests.retrieveUUID(container))
                .path("items")
                .path("abcde")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"destination\":\"" + ClientUtilsForTests.retrieveUUID(existingContainer) + "\"}"));

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
        assertThat(response.readEntity(String.class)).contains("The item you are trying to move to container 'Cave' is unknown.");
    }

}