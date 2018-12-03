package fr.bbougon.ousontmesaffaires;

import fr.bbougon.ousontmesaffaires.infrastructure.security.SecuritySettings;
import fr.bbougon.ousontmesaffaires.repositories.FileRepositories;
import io.undertow.Undertow.Builder;
import org.mongolink.Settings;

public class Configuration {

    static ServerConfiguration getServerConfiguration() {
        return FileRepositories.serverConfiguration().get();
    }

    @FunctionalInterface
    public interface ServerConfiguration {
        ServerSettings getSettings();
    }

    public interface DataBaseConfiguration {
        Settings getSettings();
    }

    public interface ServerSettings {
        Builder getBuilder();

        int getPort();

    }

    @FunctionalInterface
    public interface SecurityConfiguration {
        SecuritySettings securitySetting();

    }

    public interface RemoteServiceConfiguration {
        String nlpUrl();
    }
}
