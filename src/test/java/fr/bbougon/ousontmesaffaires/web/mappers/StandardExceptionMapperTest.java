package fr.bbougon.ousontmesaffaires.web.mappers;

import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

public class StandardExceptionMapperTest {

    @Test
    public void ifExceptionIsAWebApplicationExceptionReturnResponseFromException() {
        StandardExceptionMapper standardExceptionMapper = new StandardExceptionMapper();

        Response response = standardExceptionMapper.toResponse(new BadRequestException());

        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    }
}