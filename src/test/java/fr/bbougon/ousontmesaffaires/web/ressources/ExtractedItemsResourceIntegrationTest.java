package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.WithEmbeddedServer;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.test.utils.ClientUtilsForTests;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class ExtractedItemsResourceIntegrationTest {

    @Rule
    public WithEmbeddedServer withEmbeddedServer = new WithEmbeddedServer();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Before
    public void before() {
        Response response = ClientUtilsForTests.createContainer("json/t-shirt.json");
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
        Response firstContainer = ClientUtilsForTests.createContainer("json/t-shirt.json");
        createExtractedItem(firstContainer.getLocation().getPath().substring(firstContainer.getLocation().getPath().lastIndexOf("/") + 1),
                retrieveHash(getContainerResponse(firstContainer.getLocation()).readEntity(String.class)));
        Response secondContainer = ClientUtilsForTests.createContainer("json/t-shirt.json");
        createExtractedItem(secondContainer.getLocation().getPath().substring(secondContainer.getLocation().getPath().lastIndexOf("/") + 1),
                retrieveHash(getContainerResponse(secondContainer.getLocation()).readEntity(String.class)));

        Response response = ClientBuilder.newClient()
                .target("http://localhost:17000")
                .path("extracted-items")
                .request()
                .get(Response.class);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }

    @Test
    public void canGetExtractedItem() {
        Response container = ClientUtilsForTests.createContainer("json/t-shirt.json");
        Response response = createExtractedItem(container.getLocation().getPath().substring(container.getLocation().getPath().lastIndexOf("/") + 1),
                retrieveHash(getContainerResponse(container.getLocation()).readEntity(String.class)));

        Response extractedItem = ClientBuilder.newClient()
                .target(response.getLocation())
                .request()
                .get(Response.class);

        assertThat(extractedItem.getStatus()).isEqualTo(OK.getStatusCode());
    }



    @Test
    public void returns404OnNotFoundExtractedItem() {
        Response response = ClientBuilder.newClient()
                .target("http://localhost:17000")
                .path("extracted-items")
                .path(new Codec().urlSafeToBase64(UUID.randomUUID().toString()))
                .request()
                .get(Response.class);

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    private Response getContainerResponse(final URI containerLocation) {
        return ClientBuilder.newClient()
                .target(containerLocation)
                .request()
                .get(Response.class);
    }

    private Response createExtractedItem(final String containerId, final String itemHash) {
        return ClientBuilder.newClient()
                .target("http://localhost:17000")
                .path("extracted-items")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"containerId\":\"" + containerId + "\",\"itemHash\":\"" + itemHash + "\"}"), Response.class);
    }

    private String retrieveHash(final String payload) {
        return retrieveStringToReplace(payload);
    }

    private String retrieveStringToReplace(final String payload) {
        return payload.substring(payload.lastIndexOf("itemHash") + 11, payload.lastIndexOf("itemHash") + 51);
    }

    private URI containerLocation;
}