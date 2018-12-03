package fr.bbougon.ousontmesaffaires.web.mappers;

import fr.bbougon.ousontmesaffaires.infrastructure.TechnicalError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class TechnicalErrorMapperTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"REMOTE_SERVICE_ERROR", Response.Status.INTERNAL_SERVER_ERROR, "An error occurred while reaching a remote service."}
        });
    }

    @Parameterized.Parameter
    public String errorCode;
    @Parameterized.Parameter(1)
    public Response.Status status;
    @Parameterized.Parameter(2)
    public String expectedMessage;

    @Test
    public void canMapTechnicalErrors() {
        TechnicalError exception = new TechnicalError(errorCode);
        Response response = new TechnicalErrorMapper().toResponse(exception);
        assertThat(response.getStatus()).isEqualTo(status.getStatusCode());
        assertThat(response.getEntity()).isEqualTo("{\"error\":\"" + expectedMessage + "\"}");
    }

}