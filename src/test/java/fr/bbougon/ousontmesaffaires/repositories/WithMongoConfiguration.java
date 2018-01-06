package fr.bbougon.ousontmesaffaires.repositories;

import org.junit.rules.ExternalResource;
import org.mongolink.MongoSession;
import org.mongolink.MongoSessionManager;

import static org.mockito.Mockito.mock;

public class WithMongoConfiguration extends ExternalResource {

    public WithMongoConfiguration() {
        MongoConfiguration mongoConfiguration = mock(MongoConfiguration.class);
        mongoConfiguration.session = mock(MongoSession.class);
        mongoConfiguration.sessionManager = mock(MongoSessionManager.class);
        MongoConfiguration.SingletonHolder.INSTANCE = mongoConfiguration;
    }
}
