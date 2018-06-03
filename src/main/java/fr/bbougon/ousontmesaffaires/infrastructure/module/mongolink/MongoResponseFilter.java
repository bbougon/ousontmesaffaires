package fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink;

import com.google.inject.Inject;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class MongoResponseFilter implements ContainerResponseFilter {

    @Inject
    public MongoResponseFilter(MongolinkSessionManager mongolinkSessionManager) {
        this.mongolinkSessionManager = mongolinkSessionManager;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) {
        mongolinkSessionManager.stop();

    }

    private final MongolinkSessionManager mongolinkSessionManager;
}
