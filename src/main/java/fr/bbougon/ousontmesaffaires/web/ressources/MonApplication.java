package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.entrepot.EntrepotEmplacement;
import fr.bbougon.ousontmesaffaires.entrepot.mongo.EntrepotEmplacementMongo;
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
                bind(EntrepotEmplacementMongo.class).to(EntrepotEmplacement.class);
            }
        });
    }
}
