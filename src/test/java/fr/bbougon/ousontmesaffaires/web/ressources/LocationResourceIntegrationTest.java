package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.WithEmbeddedServer;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtils;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;
import static org.fest.assertions.api.Assertions.assertThat;

public class LocationResourceIntegrationTest {

    @Rule
    public WithEmbeddedServer withEmbeddedServer = new WithEmbeddedServer();

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
                .post(Entity.json(new FileUtils("json/t-shirt.json").getContent()), Response.class);

        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
    }

    @Test
    public void canGetLocation() {
        Response location = createLocation("json/pantalon.json");

        Response response = ClientBuilder.newClient().target(location.getLocation())
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        String locationId = location.getLocation().getPath().substring(location.getLocation().getPath().lastIndexOf("/") + 1);
        assertThat(response.readEntity(String.class)).isEqualTo("{\"id\":\""+ locationId +"\",\"location\":\"placard\",\"items\":[{\"item\":{\"taille\":\"3ans\",\"type\":\"pantalon\",\"couleur\":\"noir\"}}],\"qrcode\":\"a qr code\"}");
    }

    @Test
    public void canGetAllLocations() {
        Response location = createLocation("json/pantalon.json");

        Response response = ClientBuilder.newClient().target("http://localhost:17000")
                .path(LocationResource.PATH)
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        String locationId = location.getLocation().getPath().substring(location.getLocation().getPath().lastIndexOf("/") + 1);
        assertThat(response.readEntity(String.class)).contains("{\"id\":\""+ locationId +"\",\"location\":\"placard\",\"items\":[{\"item\":{\"taille\":\"3ans\",\"type\":\"pantalon\",\"couleur\":\"noir\"}}],\"qrcode\":\"a qr code\"}");
    }

    @Test
    public void exceptionsAreWrapped() {
        Response response = ClientBuilder.newClient().target("http://localhost:17000")
                .path("/locations")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(""), Response.class);

        assertThat(response.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
        assertThat(response.readEntity(String.class)).isEqualTo("Error has been thrown trying to access resource '/locations' (method POST): Payload cannot be empty.");
    }

    private Response createLocation(final String resourceFilePath) {
        String payload = new FileUtils(resourceFilePath).getContent();

        return ClientBuilder.newClient().target("http://localhost:17000")
                .path("/locations")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(payload), Response.class);
    }

}