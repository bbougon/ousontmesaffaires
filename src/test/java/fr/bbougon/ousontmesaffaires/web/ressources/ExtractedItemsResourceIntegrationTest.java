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
import static javax.ws.rs.core.Response.Status.OK;
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
        String containerId = containerLocation.getPath().substring(containerLocation.getPath().lastIndexOf("/") + 1);
        String itemHash = retrieveHash(getContainerResponse(containerLocation).readEntity(String.class));

        Response response = ClientBuilder.newClient()
                .target("http://localhost:17000")
                .path("extracted-items")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"containerId\":\"" + containerId + "\",\"itemHash\":\"" + itemHash + "\"}"), Response.class);

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(response.getLocation()).isNotNull();
    }

    @Test
    public void canGetAllExtractedItems() {
        Response firstContainer = createContainer();
        createExtractedItem(firstContainer.getLocation().getPath().substring(firstContainer.getLocation().getPath().lastIndexOf("/") + 1),
                retrieveHash(getContainerResponse(firstContainer.getLocation()).readEntity(String.class)));
        Response secondContainer = createContainer();
        createExtractedItem(secondContainer.getLocation().getPath().substring(secondContainer.getLocation().getPath().lastIndexOf("/") + 1),
                retrieveHash(getContainerResponse(secondContainer.getLocation()).readEntity(String.class)));

        Response response = ClientBuilder.newClient()
                .target("http://localhost:17000")
                .path("extracted-items")
                .request()
                .get(Response.class);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }

    private Response getContainerResponse(final URI containerLocation) {
        return ClientBuilder.newClient()
                .target(containerLocation)
                .request()
                .get(Response.class);
    }

    private void createExtractedItem(final String containerId, final String itemHash) {
        ClientBuilder.newClient()
                .target("http://localhost:17000")
                .path("extracted-items")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"containerId\":\"" + containerId + "\",\"itemHash\":\"" + itemHash + "\"}"), Response.class);
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