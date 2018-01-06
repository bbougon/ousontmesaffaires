package fr.bbougon.ousontmesaffaires.repositories;

import java.util.List;
import java.util.UUID;

public interface Repository<T> {

    void persist(T entity);

    List<T> getAll();

    T findById(UUID id);
}
