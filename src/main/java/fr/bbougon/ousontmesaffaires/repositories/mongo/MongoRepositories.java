package fr.bbougon.ousontmesaffaires.repositories.mongo;

import fr.bbougon.ousontmesaffaires.domain.container.ContainerRepository;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItemRepository;
import fr.bbougon.ousontmesaffaires.repositories.*;

import javax.inject.Inject;

public class MongoRepositories extends Repositories {

    @Inject
    private ContainerMongoRepository containerMongoRepository;

    @Inject
    private ExtractedItemMongoRepository extractedItemMongoRepository;

    @Override
    protected ContainerRepository getContainerRepository() {
        return containerMongoRepository;
    }

    @Override
    protected ExtractedItemRepository getExtractedItemRepository() {
        return extractedItemMongoRepository;
    }
}
