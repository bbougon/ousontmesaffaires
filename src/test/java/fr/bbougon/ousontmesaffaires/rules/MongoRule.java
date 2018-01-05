package fr.bbougon.ousontmesaffaires.rules;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import fr.bbougon.ousontmesaffaires.entrepot.MongoConfiguration;
import org.junit.rules.ExternalResource;
import org.mongolink.*;

public class MongoRule extends ExternalResource {

    @Override
    public void before() throws Throwable {
        session = MongoConfiguration.createSession(settings());
        session.start();
        super.before();
    }

    @Override
    public void after() {
        session.getDb().drop();
        session.stop();
        super.after();
    }

    private Settings settings() {
        return Settings.defaultInstance()
                .withDatabase(new MongoClient(new ServerAddress("127.0.0.1", 27017), Lists.newArrayList()).getDatabase("ousontmesaffaires"))
                .withDefaultUpdateStrategy(UpdateStrategies.DIFF);
    }

    public void cleanSession() {
        session.flush();
        session.clear();
    }

    public MongoSession session;
}
