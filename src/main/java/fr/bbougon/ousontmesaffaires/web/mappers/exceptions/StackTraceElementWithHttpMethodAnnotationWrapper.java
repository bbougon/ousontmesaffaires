package fr.bbougon.ousontmesaffaires.web.mappers.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.Optional;

public class StackTraceElementWithHttpMethodAnnotationWrapper extends StackTraceElementWithPathWrapper {

    public StackTraceElementWithHttpMethodAnnotationWrapper(final StackTraceElementWithPathWrapper stackTraceElementWithPathWrapper) {
        super(stackTraceElementWithPathWrapper.getStackTraceElement(), stackTraceElementWithPathWrapper.getPath().orElse(null));
        try {
            Arrays.stream(Class.forName(stackTraceElementWithPathWrapper.getStackTraceElement().getClassName()).getMethods())
                    .filter(method -> method.getName().equals(stackTraceElementWithPathWrapper.getStackTraceElement().getMethodName()))
                    .map(AccessibleObject::getAnnotations)
                    .map(AnnotationWrapper::new)
                    .map(AnnotationWrapper::getHttpMethod)
                    .map(Optional::get)
                    .findFirst()
                    .ifPresent(httpMethod -> this.method = httpMethod);
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
        }
    }

    public Optional<HttpMethod> getMethod() {
        return Optional.ofNullable(method);
    }

    private HttpMethod method;
    private static final Logger LOGGER = LoggerFactory.getLogger(StackTraceElementWithHttpMethodAnnotationWrapper.class);
}
