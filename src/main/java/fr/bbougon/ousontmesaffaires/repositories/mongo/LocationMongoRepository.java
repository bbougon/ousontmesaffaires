package fr.bbougon.ousontmesaffaires.repositories.mongo;

import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkSessionManager;
import fr.bbougon.ousontmesaffaires.repositories.LocationRepository;

import javax.inject.Inject;

public class LocationMongoRepository extends MongoRepository<Location> implements LocationRepository {

    @Inject
    public LocationMongoRepository(final MongolinkSessionManager mongolinkSessionManager) {
        super(mongolinkSessionManager);
    }
}
