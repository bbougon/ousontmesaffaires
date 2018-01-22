package fr.bbougon.ousontmesaffaires.rules;

import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkSessionManager;
import fr.bbougon.ousontmesaffaires.repositories.FileRepositories;
import org.junit.rules.ExternalResource;

public class MongoRule extends ExternalResource {

    @Override
    public void before() throws Throwable {
        mongolinkSessionManager = new MongolinkSessionManager("fr.bbougon.ousontmesaffaires.repositories.mongo.mapping", FileRepositories.dataBaseConfiguration().get().getSettings());
        mongolinkSessionManager.start();
        super.before();
    }

    @Override
    public void after() {
        mongolinkSessionManager.getSession().getDb().drop();
        mongolinkSessionManager.getSession().stop();
        super.after();
    }

    public void cleanSession() {
        mongolinkSessionManager.getSession().flush();
        mongolinkSessionManager.getSession().clear();
    }

    public MongolinkSessionManager mongolinkSessionManager;
}
