package fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink;

import org.mongolink.*;
import org.mongolink.domain.mapper.ContextBuilder;

public class MongolinkSessionManager {

    public MongolinkSessionManager(String mappingPackage, Settings settings) {
        sessionManager = MongoSessionManager.create(new ContextBuilder(mappingPackage), settings);
    }

    public synchronized void start() {
        if(!hasBeenStarted && hasBeenStopped) {
            session = sessionManager.createSession();
            session.start();
            hasBeenStarted = true;
            hasBeenStopped = false;
        }
    }

    public synchronized void stop() {
        if(hasBeenStarted && !hasBeenStopped) {
            session.stop();
            sessionManager.close();
            hasBeenStopped = true;
            hasBeenStarted = false;
        }
    }

    public MongoSession getSession() {
        return session;
    }

    private final MongoSessionManager sessionManager;
    private MongoSession session;
    private boolean hasBeenStarted;
    private boolean hasBeenStopped = true;
}
