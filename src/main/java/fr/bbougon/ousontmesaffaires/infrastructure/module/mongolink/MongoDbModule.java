package fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink;

import com.google.inject.*;
import org.mongolink.Settings;

public class MongoDbModule extends AbstractModule {

    MongoDbModule(final String mappingPackage, final Settings settings) {
        mongolinkSessionManager = new MongolinkSessionManager(mappingPackage, settings);
    }

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public MongolinkSessionManager mongolinkSessionManager() {
        return mongolinkSessionManager;
    }

    private final MongolinkSessionManager mongolinkSessionManager;
}
