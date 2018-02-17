package fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink;

import org.junit.Test;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MongoResponseFilterTest {

    @Test
    public void sessionIsStoppedOnResponse() throws IOException {
        MongolinkSessionManager mongolinkSessionManager = mock(MongolinkSessionManager.class);
        MongoResponseFilter mongoResponseFilter = new MongoResponseFilter(mongolinkSessionManager);

        mongoResponseFilter.filter(mock(ContainerRequestContext.class), mock(ContainerResponseContext.class));

        verify(mongolinkSessionManager).stop();
    }
}