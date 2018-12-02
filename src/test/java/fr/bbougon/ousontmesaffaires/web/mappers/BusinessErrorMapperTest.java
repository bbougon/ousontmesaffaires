package fr.bbougon.ousontmesaffaires.web.mappers;

import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class BusinessErrorMapperTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"UNEXISTING_CONTAINER", null, Response.Status.NOT_FOUND, "The container you are requesting does not exist."},
                {"UNEXISTING_DESTINATION_CONTAINER", null, Response.Status.NOT_FOUND, "The destination container you are requesting does not exist."},
                {"UNKNOWN_PATCH", "unknown", Response.Status.NOT_FOUND, "An error occurred during patch, current target 'unknown' is unknown."},
                {"UNKNOWN_ITEM_TO_PATCH", "big container", Response.Status.NOT_FOUND, "The item you are trying to patch on container 'big container' is unknown."},
                {"UNKNOWN_ITEM_TO_MOVE", "existing container", Response.Status.NOT_FOUND, "The item you are trying to move to container 'existing container' is unknown."},
                {"UNKNOWN_ITEM_TO_ANALYSE", "a container", Response.Status.NOT_FOUND, "The item you are trying to analyse in container 'a container' is unknown."}
        });
    }

    @Parameterized.Parameter
    public String errorCode;
    @Parameterized.Parameter(1)
    public String target;
    @Parameterized.Parameter(2)
    public Response.Status status;
    @Parameterized.Parameter(3)
    public String expectedMessage;

    @Test
    public void canMapBusinessErrors() {
        BusinessError exception = new BusinessError(errorCode, target);
        assertThat(new BusinessErrorMapper().toResponse(exception).getStatus()).isEqualTo(status.getStatusCode());
        assertThat(new BusinessErrorMapper().toResponse(exception).getEntity()).isEqualTo("{\"error\":\"" + expectedMessage + "\"}");
    }
}