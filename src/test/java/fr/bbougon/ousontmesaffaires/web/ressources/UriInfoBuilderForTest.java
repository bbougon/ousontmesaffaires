package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;
import org.jboss.resteasy.spi.ResteasyUriInfo;

public class UriInfoBuilderForTest {

    public ResteasyUriInfo forContainer(final String containerId) {
        return new ResteasyUriInfo(uriBuilder.withPath(containerId).build());
    }

    public ResteasyUriInfo forContainers() {
        return new ResteasyUriInfo(uriBuilder.build());
    }

    private static final String HTTP_LOCALHOST_CONTAINERS = "http://localhost/containers";
    private final URIBuilder uriBuilder = new URIBuilder().withPath(HTTP_LOCALHOST_CONTAINERS);
}
