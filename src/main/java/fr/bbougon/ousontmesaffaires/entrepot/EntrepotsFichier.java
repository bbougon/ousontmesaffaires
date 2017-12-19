package fr.bbougon.ousontmesaffaires.entrepot;

import com.google.common.collect.Maps;
import fr.bbougon.ousontmesaffaires.Configuration;
import fr.bbougon.ousontmesaffaires.Configuration.ConfigurationServeur;
import org.mongolink.Settings;

import java.util.Map;
import java.util.ResourceBundle;

import static fr.bbougon.ousontmesaffaires.Configuration.*;

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
                    return Settings.defaultInstance()
                            .withDbName(configuration.get("database.name"))
                            .withAuthentication(configuration.get("database.user"), configuration.get("database.password"))
                            .withHost(configuration.get("database.host"))
                            .withPort(Integer.parseInt(configuration.get("database.port")));
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
