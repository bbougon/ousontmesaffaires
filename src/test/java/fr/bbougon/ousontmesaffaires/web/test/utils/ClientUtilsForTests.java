package fr.bbougon.ousontmesaffaires.web.test.utils;

import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ClientUtilsForTests {

    public static String retrieveUUID(final Response response) {
        return response.getLocation().getPath().substring(response.getLocation().getPath().lastIndexOf("/") + 1);
    }

    public static Response createContainer(final String resourceFilePath) {
        String payload = new FileUtilsForTest(resourceFilePath).getContent();

        return ClientBuilder.newClient().target("http://localhost:17000")
                .path("/containers")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(payload), Response.class);
    }
}
