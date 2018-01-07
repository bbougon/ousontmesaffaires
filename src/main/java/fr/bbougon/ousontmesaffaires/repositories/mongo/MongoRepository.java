package fr.bbougon.ousontmesaffaires.repositories.mongo;

import fr.bbougon.ousontmesaffaires.repositories.Repository;
import org.mongolink.MongoSession;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.UUID;

public class MongoRepository<T> implements Repository<T> {

    MongoRepository(MongoSession session) {
        this.session = session;
    }

    public void persist(T entity) {
        session.save(entity);
        session.flush();
    }

    @Override
    public List<T> getAll() {
        return session.getAll(persistentType());
    }

    @Override
    public T findById(final UUID id) {
        return session.get(id, persistentType());
    }

    private Class<T> persistentType() {
        final ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) superclass.getActualTypeArguments()[0];
    }

    private final MongoSession session;
}
