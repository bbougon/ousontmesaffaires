package fr.bbougon.ousontmesaffaires.repositories;

import org.mongolink.*;
import org.mongolink.domain.mapper.ContextBuilder;

public class MongoConfiguration {

    public static void stopSession() {
        SingletonHolder.INSTANCE.stop();
    }

    private void stop() {
        session.stop();
        sessionManager.close();
    }

    public static MongoSession createSession(final Settings settings) {
        ContextBuilder builder = new ContextBuilder("fr.bbougon.ousontmesaffaires.repositories.mongo.mapping");
        return SingletonHolder.INSTANCE.createSession(builder, settings);
    }

    private MongoSession createSession(final ContextBuilder builder, final Settings settings) {
        sessionManager = MongoSessionManager.create(builder, settings);
        session = sessionManager.createSession();
        return session;
    }

    public static void startSession() {
        SingletonHolder.INSTANCE.session.start();
    }

    static class SingletonHolder {
        static MongoConfiguration INSTANCE = new MongoConfiguration();
    }

    MongoSessionManager sessionManager;
    MongoSession session;
}
