package fr.bbougon.ousontmesaffaires.entrepot.mongo;

import fr.bbougon.ousontmesaffaires.entrepot.Repository;
import org.mongolink.MongoSession;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class MongoRepository<T> implements Repository<T> {

    MongoRepository(MongoSession session) {
        this.session = session;
    }

    @PostConstruct
    private void startSession() {
        session.start();
    }


    public void persist(T entity) {
        session.save(entity);
        session.flush();
    }

    @Override
    public List<T> getAll() {
        return session.getAll(persistentType());
    }

    private Class<T> persistentType() {
        final ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) superclass.getActualTypeArguments()[0];
    }

    private final MongoSession session;
}
