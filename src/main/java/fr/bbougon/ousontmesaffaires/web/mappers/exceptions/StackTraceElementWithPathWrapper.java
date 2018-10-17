package fr.bbougon.ousontmesaffaires.web.mappers.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import java.util.Optional;

public class StackTraceElementWithPathWrapper {

    public StackTraceElementWithPathWrapper(final StackTraceElement stackTraceElement) {
        try {
            this.stackTraceElement = stackTraceElement;
            this.path = Class.forName(stackTraceElement.getClassName()).getAnnotation(Path.class);
        } catch (ClassNotFoundException e) {
            LOGGER.debug(e.getMessage());
        }
    }

    StackTraceElementWithPathWrapper(final StackTraceElement stackTraceElement, final Path path) {
        this.stackTraceElement = stackTraceElement;
        this.path = path;
    }

    public StackTraceElement getStackTraceElement() {
        return stackTraceElement;
    }

    public Optional<Path> getPath() {
        return Optional.of(path);
    }

    private StackTraceElement stackTraceElement;
    private Path path;
    private static final Logger LOGGER = LoggerFactory.getLogger(StackTraceElementWithPathWrapper.class);
}
