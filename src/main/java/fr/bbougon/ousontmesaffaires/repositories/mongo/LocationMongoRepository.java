package fr.bbougon.ousontmesaffaires.repositories.mongo;

import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.repositories.*;
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
