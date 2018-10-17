package fr.bbougon.ousontmesaffaires.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<T> {

    void persist(T entity);

    List<T> getAll();

    Optional<T> get(UUID id);
}
