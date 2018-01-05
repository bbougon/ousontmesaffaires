package fr.bbougon.ousontmesaffaires.entrepot.mongo;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Location;
import fr.bbougon.ousontmesaffaires.entrepot.*;
import org.mongolink.MongoSession;

public class LocationMongoRepository extends MongoRepository<Location> implements LocationRepository {

    @SuppressWarnings("UnusedDeclaration")
    public LocationMongoRepository() {
        super(MongoConfiguration.createSession(FileRepositories.dataBaseConfiguration().get().getSettings()));
    }

    LocationMongoRepository(final MongoSession currentSession) {
        super(currentSession);
    }
}
