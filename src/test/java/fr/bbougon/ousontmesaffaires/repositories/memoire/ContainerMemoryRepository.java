package fr.bbougon.ousontmesaffaires.repositories.memoire;

import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.ContainerRepository;

import java.util.Optional;
import java.util.UUID;

public class ContainerMemoryRepository extends MemoryRepository<Container> implements ContainerRepository {

    @Override
    public Container findById(final UUID id) {
        return getAll().stream()
                .filter(entity -> id.equals(entity.getId()))
                .findFirst()
                .orElse(null);
    }
}
