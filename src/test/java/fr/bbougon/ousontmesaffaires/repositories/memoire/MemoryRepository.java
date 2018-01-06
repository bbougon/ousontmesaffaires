package fr.bbougon.ousontmesaffaires.repositories.memoire;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.repositories.Repository;

import java.util.List;

public abstract class MemoryRepository<T> implements Repository<T> {

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
