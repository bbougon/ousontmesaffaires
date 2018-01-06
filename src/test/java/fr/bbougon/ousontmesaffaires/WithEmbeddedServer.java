package fr.bbougon.ousontmesaffaires;

import fr.bbougon.ousontmesaffaires.repositories.*;
import org.junit.rules.ExternalResource;
import org.mongolink.MongoSession;
import org.mongolink.Settings;

public class WithEmbeddedServer extends ExternalResource {

    @Override
    public void before() throws Exception {
        loadConfiguration();
    }

    public void start() {
        EmbeddedServer.start(Configuration.getServerConfiguration());
    }

    private void loadConfiguration() {
        FileRepositories.initialise(new FileRepositories() {
            @Override
            public FileRepository<Configuration.ServerConfiguration> getServerConfiguration() {
                return() -> new Configuration.ServerConfiguration() {
                    @Override
                    public String getDescriptor() {
                        return "src/main/package/dev/resources/web.xml";
                    }

                    @Override
                    public int getPort() {
                        return 17000;
                    }
                };
            }

            @Override
            protected FileRepository<Configuration.DataBaseConfiguration> getDataBaseConfiguration() {
                return () -> (Configuration.DataBaseConfiguration) Settings::defaultInstance;
            }
        });
    }

    @Override
    public void after() {
        MongoSession session = MongoConfiguration.createSession(FileRepositories.dataBaseConfiguration().get().getSettings());
        session.start();
        session.getDb().drop();
        session.stop();
        FileRepositories.initialise(new DefaultFileRepositories());
    }

    public String getUrl() {
        return EmbeddedServer.getUrl();
    }

    public void stop() {
        EmbeddedServer.stop();
    }
}
