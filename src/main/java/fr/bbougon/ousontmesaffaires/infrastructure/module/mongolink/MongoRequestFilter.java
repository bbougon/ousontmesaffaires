package fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink;

import com.google.inject.Inject;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class MongoRequestFilter implements ContainerRequestFilter {

    @Inject
    public MongoRequestFilter(MongolinkSessionManager mongolinkSessionManager) {
        this.mongolinkSessionManager = mongolinkSessionManager;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        mongolinkSessionManager.start();
    }

    private final MongolinkSessionManager mongolinkSessionManager;
}
