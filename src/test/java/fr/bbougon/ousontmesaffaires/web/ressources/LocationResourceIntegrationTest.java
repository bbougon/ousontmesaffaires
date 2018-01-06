package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.WithEmbeddedServer;
import fr.bbougon.ousontmesaffaires.test.utils.JsonFileUtils;
import org.junit.*;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static org.fest.assertions.api.Assertions.assertThat;

public class LocationResourceIntegrationTest {

    @Rule
    public WithEmbeddedServer withEmbeddedServer = new WithEmbeddedServer();

    @Before
    public void before() {
        withEmbeddedServer.start();
    }

    @After
    public void after() {
        withEmbeddedServer.stop();
    }

    @Test
    public void canAddLocation() {
        Response response = createLocation("json/t-shirt.json");

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
    }

    @Test
    public void canAddAnItemToLocation() {
        Response location = createLocation("json/pantalon.json");

        Response response = ClientBuilder.newClient().target(location.getLocation())
                .path("item")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new JsonFileUtils("json/t-shirt.json").getPayload()), Response.class);

        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
    }

    private Response createLocation(final String resourceFilePath) {
        String payload = new JsonFileUtils(resourceFilePath).getPayload();

        return ClientBuilder.newClient().target(withEmbeddedServer.getUrl())
                .path("/location")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(payload), Response.class);
    }

}