package fr.bbougon.ousontmesaffaires.web.helpers;

import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class URIBuilder {

    public URI build(String path) {
        try {
            return new URI(path);
        } catch (URISyntaxException e) {
            LoggerFactory.getLogger(URIBuilder.class).warn("Error while building URI for path : {}", path);
            return null;
        }
    }
}