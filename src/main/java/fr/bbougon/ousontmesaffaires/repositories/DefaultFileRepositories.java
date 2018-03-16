package fr.bbougon.ousontmesaffaires.repositories;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import fr.bbougon.ousontmesaffaires.Configuration;
import fr.bbougon.ousontmesaffaires.infrastructure.ConfigurationProperties;
import org.mongolink.Settings;
import org.mongolink.UpdateStrategies;

import java.util.List;

public class DefaultFileRepositories extends FileRepositories {

    public DefaultFileRepositories(final ConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @Override
    public FileRepository<Configuration.ServerConfiguration> getServerConfiguration() {
        return () -> (Configuration.ServerConfiguration) configurationProperties::serverPort;
    }

    @Override
    protected FileRepository<Configuration.DataBaseConfiguration> getDataBaseConfiguration() {
        return () -> (Configuration.DataBaseConfiguration) this::getSettings;
    }

    private Settings getSettings() {
        List<MongoCredential> credentialsList = getCredentials();
        return Settings.defaultInstance()
                .withDatabase(new MongoClient(new ServerAddress(configurationProperties.databaseHost(), configurationProperties.databasePort()),
                        credentialsList).getDatabase(configurationProperties.databaseName()))
                .withDefaultUpdateStrategy(UpdateStrategies.DIFF);
    }

    private List<MongoCredential> getCredentials() {
        if(configurationProperties.hasCredentials()) {
            MongoCredential credential = MongoCredential.createCredential(configurationProperties.databaseUser(), configurationProperties.databaseName(), configurationProperties.databasePassword().toCharArray());
            return Lists.newArrayList(credential);
        }
        return Lists.newArrayList();
    }

    private final ConfigurationProperties configurationProperties;
}
