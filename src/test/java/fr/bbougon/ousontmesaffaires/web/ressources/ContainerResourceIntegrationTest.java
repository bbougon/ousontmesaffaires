package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.WithEmbeddedServer;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
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
        Response response = createContainer("json/t-shirt.json");

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
    }

    @Test
    public void canAddAnItemToContainer() {
        Response container = createContainer("json/pantalon.json");

        Response response = ClientBuilder.newClient().target(container.getLocation())
                .path("item")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new FileUtilsForTest("json/t-shirt.json").getContent()), Response.class);

        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
    }

    @Test
    public void canGetContainer() {
        Response container = createContainer("json/pantalon.json");

        Response response = ClientBuilder.newClient().target(container.getLocation())
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        String containerId = container.getLocation().getPath().substring(container.getLocation().getPath().lastIndexOf("/") + 1);
        assertThat(response.readEntity(String.class)).isEqualTo("{\"id\":\"" + containerId + "\",\"name\":\"placard\",\"items\":[{\"item\":{\"taille\":\"3ans\",\"type\":\"pantalon\",\"couleur\":\"noir\"},\"hash\":\"089819bc695cf1d7c4f705f56451682d14e0b0a2\"}],\"qrcode\":\"http://localhost:17000/containers/" + containerId + "\"}");
    }

    @Test
    public void canGetAllContainers() {
        Response container = createContainer("json/pantalon.json");

        Response response = ClientBuilder.newClient().target("http://localhost:17000")
                .path(ContainerResource.PATH)
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        String containerId = container.getLocation().getPath().substring(container.getLocation().getPath().lastIndexOf("/") + 1);
        assertThat(response.readEntity(String.class)).contains("{\"id\":\"" + containerId + "\",\"name\":\"placard\",\"items\":[" +
                "{\"item\":{\"taille\":\"3ans\",\"type\":\"pantalon\",\"couleur\":\"noir\"},\"hash\":\"089819bc695cf1d7c4f705f56451682d14e0b0a2\"}]," +
                "\"qrcode\":\"http://localhost:17000/containers/" + containerId + "\"}");
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
    public void canAddADescriptionToContainer() {
        Response container = createContainer("json/pantalon.json");

        Response response = ClientBuilder.newClient().target(container.getLocation())
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .method("PATCH", Entity.json("{\"fields\":[{\"fieldName\":\"description\",\"value\":\"A description\"}]}"), Response.class);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        String containerId = container.getLocation().getPath().substring(container.getLocation().getPath().lastIndexOf("/") + 1);
        assertThat(response.readEntity(String.class)).isEqualTo(new FileUtilsForTest("json/expectedJsonResultWithDescription.json").getContent().replace("ID_TO_REPLACE", containerId));
    }

    private Response createContainer(final String resourceFilePath) {
        String payload = new FileUtilsForTest(resourceFilePath).getContent();

        return ClientBuilder.newClient().target("http://localhost:17000")
                .path("/containers")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(payload), Response.class);
    }

}