package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;
import org.jboss.resteasy.spi.ResteasyUriInfo;

public class UriInfoBuilderForTest {

    public ResteasyUriInfo forLocation(final String locationId) {
        return new ResteasyUriInfo(uriBuilder.withPath(locationId).build());
    }

    public ResteasyUriInfo forStickers(final String locationId) {
        return new ResteasyUriInfo(uriBuilder.withPath(locationId).withPath("stickers").build());
    }

    public ResteasyUriInfo forLocations() {
        return new ResteasyUriInfo(uriBuilder.build());
    }

    private static final String HTTP_LOCALHOST_LOCATIONS = "http://localhost/locations";
    private final URIBuilder uriBuilder = new URIBuilder().withPath(HTTP_LOCALHOST_LOCATIONS);
}
