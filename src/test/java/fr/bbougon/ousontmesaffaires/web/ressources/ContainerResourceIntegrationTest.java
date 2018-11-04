package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.WithEmbeddedServer;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.web.test.utils.ClientUtilsForTests;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ContainerResourceIntegrationTest {

    @Rule
    public WithEmbeddedServer withEmbeddedServer = new WithEmbeddedServer();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void canAddContainer() {
        Response response = ClientUtilsForTests.createContainer("json/t-shirt.json");

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
    }

    @Test
    public void canAddAnItemToContainer() {
        Response container = ClientUtilsForTests.createContainer("json/pantalon.json");

        Response response = ClientBuilder.newClient().target(container.getLocation())
                .path("item")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new FileUtilsForTest("json/t-shirt.json").getContent()), Response.class);

        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
    }

    @Test
    public void canGetContainer() {
        Response container = ClientUtilsForTests.createContainer("json/pantalon.json");

        Response response = ClientBuilder.newClient().target(container.getLocation())
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        String containerId = container.getLocation().getPath().substring(container.getLocation().getPath().lastIndexOf("/") + 1);
        String payload = response.readEntity(String.class);
        assertThat(payload).isEqualTo("{\"id\":\"" + containerId + "\",\"name\":\"placard\",\"items\":[{\"item\":\"pantalon noir 3ans\"," +
                "\"imageStore\":{\"folder\":\"" + retrieveFolder(payload) + "\",\"images\":[]}," +
                "\"itemHash\":\"" + retrieveHash(payload) + "\",\"features\":[]}]}");
    }

    @Test
    public void canGetAllContainers() {
        Response container = ClientUtilsForTests.createContainer("json/pantalon.json");

        Response response = ClientBuilder.newClient()
                .target("http://localhost:17000")
                .path("/containers")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        String containerId = container.getLocation().getPath().substring(container.getLocation().getPath().lastIndexOf("/") + 1);
        String payload = response.readEntity(String.class);
        assertThat(payload).contains("{\"id\":\"" + containerId + "\",\"name\":\"placard\",\"items\":[" +
                "{\"item\":\"pantalon noir 3ans\"," +
                "\"imageStore\":{\"folder\":\"" + retrieveFolder(payload) + "\",\"images\":[]}," +
                "\"itemHash\":\"" + retrieveHash(payload) + "\",\"features\":[]}]}");
    }

    @Test
    public void exceptionsAreWrapped() {
        Response response = ClientBuilder.newClient().target("http://localhost:17000")
                .path("/containers")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(""), Response.class);

        assertThat(response.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
        assertThat(response.readEntity(String.class)).isEqualTo("Error has been thrown trying to access resource '/containers' (method POST): Payload cannot be empty.");
    }

    @Test
    public void canPatchAContainerWithDescription() {
        Response container = ClientUtilsForTests.createContainer("json/pantalon.json");

        Response response = ClientBuilder.newClient().target(container.getLocation())
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .method("PATCH", Entity.json("{\"target\":\"item.description\",\"id\":\"\",\"version\":1,\"data\":\"A description\"}"), Response.class);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        String containerId = container.getLocation().getPath().substring(container.getLocation().getPath().lastIndexOf("/") + 1);
        String payload = response.readEntity(String.class);
        assertThat(payload).isEqualTo(new FileUtilsForTest("json/expectedJsonResultWithDescription.json").getContent()
                .replace("ID_TO_REPLACE", containerId)
                .replace("FOLDER_NAME", retrieveFolder(payload))
                .replace("HASH", retrieveHash(payload)));
    }

    @Test
    public void canMoveAnItemToAnExistingContainer() {
        Response container = ClientUtilsForTests.createContainer("json/pantalon.json");
        Response existingContainer = ClientUtilsForTests.createContainer("json/t-shirt.json");
        Response getContainer = ClientBuilder.newClient().target(container.getLocation())
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();
        String itemHash = retrieveHash(getContainer.readEntity(String.class));
        String existingContainerId = existingContainer.getLocation().getPath().substring(container.getLocation().getPath().lastIndexOf("/") + 1);

        Response response = ClientBuilder.newClient().target(container.getLocation())
                .path("items")
                .path(itemHash)
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"destination\":\"" + existingContainerId + "\"}"), Response.class);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.readEntity(String.class)).isNotNull();
    }

    private String retrieveFolder(final String payload) {
        return retrieveStringToReplace(payload, "folder", 9, 57);
    }

    private String retrieveHash(final String payload) {
        return retrieveStringToReplace(payload, "itemHash", 11, 51);
    }

    private String retrieveStringToReplace(final String payload, final String stringToFind, final int step, final int secondStep) {
        return payload.substring(payload.lastIndexOf(stringToFind) + step, payload.lastIndexOf(stringToFind) + secondStep);
    }

}