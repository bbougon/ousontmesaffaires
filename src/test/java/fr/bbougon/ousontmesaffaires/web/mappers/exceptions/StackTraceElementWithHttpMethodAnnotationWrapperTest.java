package fr.bbougon.ousontmesaffaires.web.mappers.exceptions;

import ch.qos.logback.classic.Level;
import fr.bbougon.ousontmesaffaires.test.utils.TestAppender;
import org.junit.Test;

import javax.ws.rs.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class StackTraceElementWithHttpMethodAnnotationWrapperTest {

    @Test
    public void log() {
        new StackTraceElementWithHttpMethodAnnotationWrapper(new StackTraceElementWithPathWrapper(new StackTraceElement("class", "method", "file", 1), mock(Path.class)));

        assertThat(TestAppender.hasMessageInLevel(Level.DEBUG, "class")).isTrue();
    }
}