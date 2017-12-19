package fr.bbougon.ousontmesaffaires.entrepot.memoire;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.entrepot.Entrepot;

import java.util.List;

public abstract class EntrepotMemoire<T> implements Entrepot<T> {

    List<T> liste = Lists.newArrayList();

    @Override
    public void persiste(T entity) {
        liste.add(entity);
    }

}
