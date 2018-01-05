package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.entrepot.LocationRepository;
import fr.bbougon.ousontmesaffaires.entrepot.mongo.LocationRepositoryMongo;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class MonApplication extends ResourceConfig {

    public MonApplication() {
        packages("fr.bbougon.ousontmesaffaires.boundaries");
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(LocationRepositoryMongo.class).to(LocationRepository.class);
            }
        });
    }
}
