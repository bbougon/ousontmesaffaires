package fr.bbougon.ousontmesaffaires.entrepot;

import java.util.List;

public interface Entrepot<T> {

    void persiste(T entity);

    List<T> getAll();
}
