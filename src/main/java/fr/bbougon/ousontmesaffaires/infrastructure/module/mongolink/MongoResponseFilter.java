package fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink;

import com.google.inject.Inject;

import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class MongoResponseFilter implements ContainerResponseFilter {

    @Inject
    public MongoResponseFilter(MongolinkSessionManager mongolinkSessionManager) {
        this.mongolinkSessionManager = mongolinkSessionManager;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) throws IOException {
        mongolinkSessionManager.stop();

    }

    private final MongolinkSessionManager mongolinkSessionManager;
}
