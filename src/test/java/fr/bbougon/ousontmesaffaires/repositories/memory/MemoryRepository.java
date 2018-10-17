package fr.bbougon.ousontmesaffaires.repositories.memory;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.domain.AggregateRoot;
import fr.bbougon.ousontmesaffaires.repositories.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class MemoryRepository<T extends AggregateRoot> implements Repository<T> {

    @Override
    public Optional<T> get(final UUID id) {
        return getAll().stream()
                .filter(entity -> id.equals(entity.getId()))
                .findFirst();
    }

    private List<T> entities = Lists.newArrayList();

    @Override
    public void persist(T entity) {
        entities.add(entity);
    }

    @Override
    public List<T> getAll() {
        return entities;
    }

}
