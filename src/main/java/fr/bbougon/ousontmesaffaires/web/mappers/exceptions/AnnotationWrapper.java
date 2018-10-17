package fr.bbougon.ousontmesaffaires.web.mappers.exceptions;

import com.google.common.collect.Lists;

import javax.ws.rs.HttpMethod;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

class AnnotationWrapper {

    public AnnotationWrapper(final Annotation[] annotations) {
        this.annotations = Lists.newArrayList(annotations);
    }

    public Optional<HttpMethod> getHttpMethod() {
        return annotations.stream()
                .filter(annotation -> annotation.annotationType().getAnnotation(HttpMethod.class) != null)
                .map(annotation -> annotation.annotationType().getAnnotation(HttpMethod.class))
                .findFirst();
    }

    private final List<Annotation> annotations;
}
