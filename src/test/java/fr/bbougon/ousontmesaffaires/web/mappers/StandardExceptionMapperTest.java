package fr.bbougon.ousontmesaffaires.web.mappers;

import fr.bbougon.ousontmesaffaires.web.ressources.DummyResource;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.assertj.core.api.Assertions.assertThat;

public class StandardExceptionMapperTest {

    @Test
    public void handleExceptionsThrownFromWebResourcePackage() {
        StandardExceptionMapper standardExceptionMapper = new StandardExceptionMapper();

        Response response = standardExceptionMapper.toResponse(new DummyResource().exception());

        assertThat(response.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.getEntity()).isEqualTo("Error has been thrown trying to access resource 'a-path' (method GET): an exception");
    }
}