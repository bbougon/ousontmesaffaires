package fr.bbougon.ousontmesaffaires.repositories.mongo;

import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkSessionManager;
import fr.bbougon.ousontmesaffaires.domain.container.ContainerRepository;

import javax.inject.Inject;

public class ContainerMongoRepository extends MongoRepository<Container> implements ContainerRepository {

    @Inject
    public ContainerMongoRepository(final MongolinkSessionManager mongolinkSessionManager) {
        super(mongolinkSessionManager);
    }
}
