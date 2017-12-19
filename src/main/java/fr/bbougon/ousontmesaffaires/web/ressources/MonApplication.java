package fr.bbougon.ousontmesaffaires.web.ressources;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class MonApplication extends ResourceConfig {

    public MonApplication() {
        packages("fr.bbougon.ousontmesaffaires.boundaries");
    }
}
