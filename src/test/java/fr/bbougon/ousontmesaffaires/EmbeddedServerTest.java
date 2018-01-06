package fr.bbougon.ousontmesaffaires;

import ch.qos.logback.classic.Level;
import fr.bbougon.ousontmesaffaires.test.utils.TestAppender;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class EmbeddedServerTest {

    @Test
    public void exceptionsAreLogged() {
        try {
            EmbeddedServer.start(new Configuration.ServerConfiguration() {
                @Override
                public String getDescriptor() {
                    return "non-existing-file-descriptor.xml";
                }

                @Override
                public int getPort() {
                    return 66000;
                }
            });
            assertThat(TestAppender.events).isNotEmpty();
            assertThat(TestAppender.getFormattedMessage(Level.ERROR, 0)).isEqualTo("Impossible to start server with descriptor file 'non-existing-file-descriptor.xml' on port '66000'");
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

}