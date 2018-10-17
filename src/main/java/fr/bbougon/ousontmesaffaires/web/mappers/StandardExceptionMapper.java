package fr.bbougon.ousontmesaffaires.web.mappers;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.web.mappers.exceptions.StackTraceElementWithHttpMethodAnnotationWrapper;
import fr.bbougon.ousontmesaffaires.web.mappers.exceptions.StackTraceElementWithPathWrapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Provider
public class StandardExceptionMapper implements ExceptionMapper<Throwable> {


    @Override
    public Response toResponse(final Throwable throwable) {
        if (throwable instanceof WebApplicationException) {
            return ((WebApplicationException) throwable).getResponse();
        }
        if(throwable instanceof BusinessError) {
            return Response
                    .status(INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(throwable.getMessage())
                    .build();
        }
        String packageName = "fr.bbougon.ousontmesaffaires.web.ressources";
        List<String> message = Lists.newArrayList();
        Lists.newArrayList(throwable.getStackTrace())
                .stream()
                .filter(stackTraceElement -> stackTraceElement.getClassName().contains(packageName))
                .map(StackTraceElementWithPathWrapper::new)
                .map(StackTraceElementWithHttpMethodAnnotationWrapper::new)
                .forEach(element -> {
                    element.getPath().ifPresent(path -> message.add(0, String.format("%s%s%s","'", path.value(), "'")));
                    element.getMethod().ifPresent(httpMethod -> message.add(1, String.format("%s%s%s", "(method ",httpMethod.value(), "): ")));
                });
        return Response
                .status(INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(String.format("%s%s %s%s", RESOURCE_MESSAGE, message.get(0), message.get(1), throwable.getMessage()))
                .build();
    }

    private static final String RESOURCE_MESSAGE = "Error has been thrown trying to access resource ";
}
