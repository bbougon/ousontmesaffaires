package fr.bbougon.ousontmesaffaires.web.mappers;

import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

public class WebApplicationExceptionMapperTest {

    @Test
    public void ifExceptionIsAWebApplicationExceptionReturnResponseFromException() {
        WebApplicationExceptionMapper webApplicationExceptionMapper = new WebApplicationExceptionMapper();

        Response response = webApplicationExceptionMapper.toResponse(new BadRequestException());

        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    }
}