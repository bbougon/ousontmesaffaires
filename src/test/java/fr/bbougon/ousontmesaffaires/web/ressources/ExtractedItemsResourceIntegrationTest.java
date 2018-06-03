package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.WithEmbeddedServer;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.CREATED;
import static org.assertj.core.api.Assertions.assertThat;

public class ExtractedItemsResourceIntegrationTest {

    @Rule
    public WithEmbeddedServer withEmbeddedServer = new WithEmbeddedServer();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Before
    public void before() {
        Response response = createContainer();
        containerLocation = response.getLocation();
    }

    @Test
    public void canAddExtractedItem() {
        String query = containerLocation.getPath().substring(containerLocation.getPath().lastIndexOf("/") + 1);
        Response containerResponse = ClientBuilder.newClient()
                .target(containerLocation)
                .request()
                .get(Response.class);
        String itemHash = retrieveHash(containerResponse.readEntity(String.class));

        Response response = ClientBuilder.newClient()
                .target("http://localhost:17000")
                .path("extracted-items")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"containerId\":\"" + query + "\",\"itemHash\":\"" + itemHash + "\"}"), Response.class);

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(response.getLocation()).isNotNull();
    }

    private Response createContainer() {
        String payload = new FileUtilsForTest("json/t-shirt.json").getContent();

        return ClientBuilder.newClient().target("http://localhost:17000")
                .path("/containers")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(payload), Response.class);
    }

    private String retrieveHash(final String payload) {
        return retrieveStringToReplace(payload);
    }

    private String retrieveStringToReplace(final String payload) {
        return payload.substring(payload.lastIndexOf("hash") + 7, payload.lastIndexOf("hash") + 47);
    }

    private URI containerLocation;
}