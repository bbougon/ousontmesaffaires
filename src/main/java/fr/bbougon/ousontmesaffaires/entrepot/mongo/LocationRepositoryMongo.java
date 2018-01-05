package fr.bbougon.ousontmesaffaires.entrepot.mongo;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Location;
import fr.bbougon.ousontmesaffaires.entrepot.*;
import org.mongolink.MongoSession;

public class LocationRepositoryMongo extends EntrepotMongo<Location> implements LocationRepository {

    @SuppressWarnings("UnusedDeclaration")
    public LocationRepositoryMongo() {
        super(MongoConfiguration.createSession(EntrepotsFichier.configurationBaseDeDonnees().get().getSettings()));
    }

    LocationRepositoryMongo(final MongoSession currentSession) {
        super(currentSession);
    }
}
