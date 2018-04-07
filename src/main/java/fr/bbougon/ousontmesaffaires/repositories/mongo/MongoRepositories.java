package fr.bbougon.ousontmesaffaires.repositories.mongo;

import fr.bbougon.ousontmesaffaires.domain.container.ContainerRepository;
import fr.bbougon.ousontmesaffaires.repositories.*;

import javax.inject.Inject;

public class MongoRepositories extends Repositories {

    @Inject
    private ContainerMongoRepository containerMongoRepository;

    @Override
    protected ContainerRepository getContainerRepository() {
        return containerMongoRepository;
    }
}
