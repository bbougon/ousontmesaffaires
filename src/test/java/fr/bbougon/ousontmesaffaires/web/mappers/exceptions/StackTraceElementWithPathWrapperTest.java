package fr.bbougon.ousontmesaffaires.web.mappers.exceptions;

import ch.qos.logback.classic.Level;
import fr.bbougon.ousontmesaffaires.test.utils.TestAppender;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StackTraceElementWithPathWrapperTest {

    @Test
    public void log() {
        new StackTraceElementWithPathWrapper(new StackTraceElement("class", "method", "file", 1));

        assertThat(TestAppender.hasMessageInLevel(Level.DEBUG, "class")).isTrue();
    }
}