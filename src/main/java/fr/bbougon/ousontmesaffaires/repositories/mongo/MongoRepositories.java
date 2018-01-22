package fr.bbougon.ousontmesaffaires.repositories.mongo;

import fr.bbougon.ousontmesaffaires.repositories.*;

import javax.inject.Inject;

public class MongoRepositories extends Repositories {

    @Inject
    private LocationMongoRepository locationMongoRepository;

    @Override
    protected LocationRepository getLocationRepository() {
        return locationMongoRepository;
    }
}
