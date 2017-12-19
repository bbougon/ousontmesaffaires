package fr.bbougon.ousontmesaffaires.entrepot;

import fr.bbougon.ousontmesaffaires.Proprietes;
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

    static MongoSession startSession(final Settings settings) {
        ContextBuilder builder = new ContextBuilder("fr.bbougon.ousontmesaffaires.entrepot.mongo.mapping");
        return SingletonHolder.INSTANCE.startSession(builder, settings);
    }

    private MongoSession startSession(final ContextBuilder builder, final Settings settings) {
        sessionManager = MongoSessionManager.create(builder, new Proprietes().ajouteLesProprietes(settings));
        session = sessionManager.createSession();
        session.start();
        return session;
    }

    private static class SingletonHolder {
        static final MongoConfiguration INSTANCE = new MongoConfiguration();
    }

    private MongoSessionManager sessionManager;
    private MongoSession session;
}
