package fr.bbougon.ousontmesaffaires.rules;

import fr.bbougon.ousontmesaffaires.*;
import fr.bbougon.ousontmesaffaires.entrepot.*;
import org.junit.rules.ExternalResource;
import org.mongolink.MongoSession;
import org.mongolink.Settings;

public class AvecServeurEmbarque extends ExternalResource {

    @Override
    public void before() throws Exception {
        loadConfiguration();
        Main.main(new String[]{""});
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
                        return 8080;
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
    }

    public String getUrl() {
        return Serveur.getUrl();
    }

}
