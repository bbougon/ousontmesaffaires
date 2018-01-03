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
        EntrepotsFichier.initialise(new EntrepotsFichier() {
            @Override
            public EntrepotFichier<Configuration.ConfigurationServeur> getConfigurationServeur() {
                return() -> new Configuration.ConfigurationServeur() {
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
            protected EntrepotFichier<Configuration.ConfigurationBaseDeDonnees> getConfigurationBaseDeDonnees() {
                return () -> (Configuration.ConfigurationBaseDeDonnees) Settings::defaultInstance;
            }
        });
    }

    @Override
    public void after() {
        MongoSession session = MongoConfiguration.createSession(EntrepotsFichier.configurationBaseDeDonnees().get().getSettings());
        session.start();
        session.getDb().dropDatabase();
        session.stop();
    }

    public String getUrl() {
        return Serveur.getUrl();
    }

}
