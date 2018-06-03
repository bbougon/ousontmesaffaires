package fr.bbougon.ousontmesaffaires.repositories.mongo;

import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItemRepository;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkSessionManager;

import javax.inject.Inject;

public class ExtractedItemMongoRepository extends MongoRepository<ExtractedItem> implements ExtractedItemRepository {

    @Inject
    public ExtractedItemMongoRepository(final MongolinkSessionManager mongolinkSessionManager) {
        super(mongolinkSessionManager);
    }
}
