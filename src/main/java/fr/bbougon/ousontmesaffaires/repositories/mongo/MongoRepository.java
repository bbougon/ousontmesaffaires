package fr.bbougon.ousontmesaffaires.repositories.mongo;

import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkSessionManager;
import fr.bbougon.ousontmesaffaires.repositories.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.UUID;

public class MongoRepository<T> implements Repository<T> {

    MongoRepository(MongolinkSessionManager mongolinkSessionManager) {
        this.mongolinkSessionManager = mongolinkSessionManager;
    }

    public void persist(T entity) {
        mongolinkSessionManager.getSession().save(entity);
        mongolinkSessionManager.getSession().flush();
    }

    @Override
    public List<T> getAll() {
        return mongolinkSessionManager.getSession().getAll(persistentType());
    }

    @Override
    public T findById(final UUID id) {
        return mongolinkSessionManager.getSession().get(id, persistentType());
    }

    private Class<T> persistentType() {
        final ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) superclass.getActualTypeArguments()[0];
    }

    private final MongolinkSessionManager mongolinkSessionManager;
}
