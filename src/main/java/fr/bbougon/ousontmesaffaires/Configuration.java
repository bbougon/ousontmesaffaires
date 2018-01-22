package fr.bbougon.ousontmesaffaires;

import fr.bbougon.ousontmesaffaires.repositories.FileRepositories;
import org.mongolink.Settings;

public class Configuration {

    static ServerConfiguration getServerConfiguration() {
        return FileRepositories.serverConfiguration().get();
    }

    public interface ServerConfiguration {

        int getPort();

    }

    public interface DataBaseConfiguration {

        Settings getSettings();
    }
}
