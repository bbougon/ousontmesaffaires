package fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink;

import org.junit.Test;

import javax.ws.rs.container.ContainerRequestContext;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MongoRequestFilterTest {

    @Test
    public void startSessionOnRequestFilter() throws IOException {
        MongolinkSessionManager mongolinkSessionManager = mock(MongolinkSessionManager.class);
        MongoRequestFilter mongoRequestFilter = new MongoRequestFilter(mongolinkSessionManager);

        mongoRequestFilter.filter(mock(ContainerRequestContext.class));

        verify(mongolinkSessionManager).start();
    }
}