package fr.bbougon.ousontmesaffaires.web.helpers;

import java.net.URI;
import java.net.URISyntaxException;

public class URIBuilder {

    public URI build(String path) {
        try {
            return new URI(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
