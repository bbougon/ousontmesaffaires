package fr.bbougon.ousontmesaffaires.repositories;

import fr.bbougon.ousontmesaffaires.domain.container.ContainerRepository;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItemRepository;
import fr.bbougon.ousontmesaffaires.repositories.memory.ContainerMemoryRepository;
import fr.bbougon.ousontmesaffaires.repositories.memory.ExtractedItemMemoryRepository;

public class MemoryRepositories extends Repositories {

    MemoryRepositories() {
    }

    @Override
    protected ContainerRepository getContainerRepository() {
        return containerMemoryRepository;
    }

    @Override
    protected ExtractedItemRepository getExtractedItemRepository() {
        return extractedItemMemoryRepository;
    }

    private final ContainerMemoryRepository containerMemoryRepository = new ContainerMemoryRepository();
    private final ExtractedItemRepository extractedItemMemoryRepository = new ExtractedItemMemoryRepository();
}
