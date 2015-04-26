package fr.bbougon.ousontmesaffaires.entrepot;

import java.util.List;

public interface Entrepot<T> {
    List<T> tous();
    void persiste(T entity);
}
