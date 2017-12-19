package fr.bbougon.ousontmesaffaires.entrepot;

public interface Entrepot<T> {

    void persiste(T entity);
}
