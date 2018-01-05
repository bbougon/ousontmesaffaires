package fr.bbougon.ousontmesaffaires.entrepot.memoire;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.entrepot.Repository;

import java.util.List;

public abstract class MemoryRepository<T> implements Repository<T> {

    private List<T> liste = Lists.newArrayList();

    @Override
    public void persist(T entity) {
        liste.add(entity);
    }

    @Override
    public List<T> getAll() {
        return liste;
    }
}
