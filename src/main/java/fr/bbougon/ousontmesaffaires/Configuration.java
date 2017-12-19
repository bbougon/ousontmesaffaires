package fr.bbougon.ousontmesaffaires;

import fr.bbougon.ousontmesaffaires.entrepot.EntrepotsFichier;
import org.mongolink.Settings;

public class Configuration {

    public static ConfigurationServeur getConfigurationServeur() {
        return EntrepotsFichier.configurationServeur().get();
    }

    public static ConfigurationBaseDeDonnees getConfigurationBaseDeDonnees() {
        return EntrepotsFichier.configurationBaseDeDonnees().get();
    }


    public interface ConfigurationServeur {
        String getDescriptor();

        int getPort();
    }

    public interface ConfigurationBaseDeDonnees {

        Settings getSettings();
    }
}
