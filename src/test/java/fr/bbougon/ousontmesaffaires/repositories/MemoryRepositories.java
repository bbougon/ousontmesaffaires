package fr.bbougon.ousontmesaffaires.repositories;

import fr.bbougon.ousontmesaffaires.domain.container.ContainerRepository;
import fr.bbougon.ousontmesaffaires.repositories.memoire.ContainerMemoryRepository;

public class MemoryRepositories extends Repositories {

    MemoryRepositories() {
    }

    @Override
    protected ContainerRepository getContainerRepository() {
        return containerMemoryRepository;
    }

    private final ContainerMemoryRepository containerMemoryRepository = new ContainerMemoryRepository();
}
