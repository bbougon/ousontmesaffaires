package fr.bbougon.ousontmesaffaires.entrepot;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import fr.bbougon.ousontmesaffaires.Configuration.ConfigurationServeur;
import org.mongolink.Settings;
import org.mongolink.UpdateStrategies;

import java.util.Map;
import java.util.ResourceBundle;

import static fr.bbougon.ousontmesaffaires.Configuration.ConfigurationBaseDeDonnees;

public abstract class EntrepotsFichier {

    public static void initialise(EntrepotsFichier entrepots) {
        SingletonHolder.INSTANCE = entrepots;
    }

    public static EntrepotFichier<ConfigurationServeur> configurationServeur() {
        return SingletonHolder.INSTANCE.getConfigurationServeur();
    }

    public abstract EntrepotFichier<ConfigurationServeur> getConfigurationServeur();

    public static EntrepotFichier<ConfigurationBaseDeDonnees> configurationBaseDeDonnees() {
        return SingletonHolder.INSTANCE.getConfigurationBaseDeDonnees();
    }

    protected abstract EntrepotFichier<ConfigurationBaseDeDonnees> getConfigurationBaseDeDonnees();

    private static class SingletonHolder {
        static EntrepotsFichier INSTANCE = new EntrepotsFichier() {
            @Override
            public EntrepotFichier<ConfigurationServeur> getConfigurationServeur() {
                return() -> new ConfigurationServeur() {
                    @Override
                    public String getDescriptor() {
                        return bundleMapped().get("serveur.descriptor");
                    }

                    @Override
                    public int getPort() {
                        return Integer.parseInt(bundleMapped().get("serveur.port"));
                    }
                };
            }

            @Override
            protected EntrepotFichier<ConfigurationBaseDeDonnees> getConfigurationBaseDeDonnees() {
                return () -> (ConfigurationBaseDeDonnees) () -> {
                    Map<String, String> configuration = bundleMapped();
                    String host = configuration.get("database.host");
                    int port = Integer.parseInt(configuration.get("database.port"));
                    //String user = configuration.get("database.user");
                    String dataBase = configuration.get("database.name");
                    //String password = configuration.get("database.password");
                    //MongoCredential credential = MongoCredential.createCredential(user, dataBase, password.toCharArray());
                    return Settings.defaultInstance()
                            .withDatabase(new MongoClient(new ServerAddress(host, port), Lists.newArrayList()).getDatabase(dataBase))
                            .withDefaultUpdateStrategy(UpdateStrategies.DIFF);
                };
            }

            private Map<String, String> bundleMapped() {
                ResourceBundle bundle = ResourceBundle.getBundle("configuration");
                Map<String, String> result = Maps.newHashMap();
                bundle.keySet().forEach(key -> result.put(key, bundle.getString(key)));
                return result;
            }
        };
    }
}
