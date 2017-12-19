package fr.bbougon.ousontmesaffaires.entrepot.mongo;

import fr.bbougon.ousontmesaffaires.entrepot.Entrepot;
import org.mongolink.MongoSession;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class EntrepotMongo<T> implements Entrepot<T> {

    public EntrepotMongo(MongoSession session) {
        this.session = session;
    }

    public void persiste(T entity) {
        session.save(entity);
    }

    protected final Class<T> persistentType() {
        final ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) superclass.getActualTypeArguments()[0];
    }

    protected final MongoSession session;
}
