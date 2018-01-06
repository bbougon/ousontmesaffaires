package fr.bbougon.ousontmesaffaires.repositories;

import java.util.List;

public interface Repository<T> {

    void persist(T entity);

    List<T> getAll();
}
