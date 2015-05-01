package fr.bbougon.ousontmesaffaires.entrepot;

import fr.bbougon.ousontmesaffaires.Proprietes;
import org.mongolink.MongoSession;
import org.mongolink.MongoSessionManager;
import org.mongolink.Settings;
import org.mongolink.domain.mapper.ContextBuilder;

public class MongoConfiguration {

    public static void stopSession() {
        session.stop();
        sessionManager.close();
    }

    public static MongoSession startSession() {
        ContextBuilder builder = new ContextBuilder("fr.bbougon.ousontmesaffaires.entrepot.mongo.mapping");
        sessionManager = MongoSessionManager.create(builder, new Proprietes().ajouteLesProprietes(Settings.defaultInstance()));
        session = sessionManager.createSession();
        session.start();
        return session;
    }

    static MongoSessionManager sessionManager;
    static MongoSession session;
}
