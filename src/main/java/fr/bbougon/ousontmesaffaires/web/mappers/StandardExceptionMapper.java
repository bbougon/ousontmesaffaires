package fr.bbougon.ousontmesaffaires.web.mappers;

import com.google.common.collect.Lists;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.List;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Provider
public class StandardExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(final Throwable throwable) {
        if (throwable instanceof WebApplicationException) {
            return ((WebApplicationException) throwable).getResponse();
        }
        StringBuilder messageBuilder = new StringBuilder();
        String packageName = "fr.bbougon.ousontmesaffaires.web.ressources";
        Lists.newArrayList(throwable.getStackTrace())
                .stream()
                .filter(stackTraceElement -> stackTraceElement.getClassName().contains(packageName))
                .forEach((StackTraceElement stackTraceElement) -> {
                    try {
                        Class<?> aClass = Class.forName(stackTraceElement.getClassName());
                        Path path = aClass.getAnnotation(Path.class);
                        if (path != null) {
                            if (messageBuilder.indexOf(RESOURCE_MESSAGE) == -1) {
                                messageBuilder.append(RESOURCE_MESSAGE);
                                messageBuilder.append("'").append(path.value()).append("'");
                            }
                            List<Method> methods = Lists.newArrayList(aClass.getMethods());
                            methods.stream()
                                    .filter(method -> method.getName().equals(stackTraceElement.getMethodName()))
                                    .forEach(method -> Lists.newArrayList(method.getAnnotations())
                                            .forEach(annotation -> {
                                                HttpMethod httpMethod = annotation.annotationType().getAnnotation(HttpMethod.class);
                                                if (httpMethod != null) {
                                                    messageBuilder.append(" (method ").append(httpMethod.value()).append("): ");
                                                }
                                            }));
                        }
                    } catch (ClassNotFoundException e) {
                        LoggerFactory.getLogger(StandardExceptionMapper.class).info("Error trying to retrieve informations from resource {}", e.getMessage());
                    }
                });
        return Response
                .status(INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(messageBuilder.append(throwable.getMessage()).toString())
                .build();
    }

    private static final String RESOURCE_MESSAGE = "Error has been thrown trying to access resource ";
}
