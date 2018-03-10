package fr.bbougon.ousontmesaffaires.web.helpers;

import com.google.common.collect.Lists;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class URIBuilder {

    public URI build(String path) {
        try {
            return new URI(path);
        } catch (URISyntaxException e) {
            LoggerFactory.getLogger(URIBuilder.class).warn("Error while building URI for path : {}", path);
            return null;
        }
    }

    public URIBuilder withPath(final String path) {
        this.paths.add(path);
        return this;
    }

    public URI build() {
        StringBuilder finalPath = new StringBuilder();
        paths.forEach(path -> finalPath.append("/").append(path));
        return build(finalPath.substring(1, finalPath.length()));
    }

    private List<String> paths = Lists.newArrayList();
}
