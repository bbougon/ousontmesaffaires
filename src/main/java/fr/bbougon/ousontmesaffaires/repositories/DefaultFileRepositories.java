package fr.bbougon.ousontmesaffaires.repositories;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import fr.bbougon.ousontmesaffaires.Configuration;
import fr.bbougon.ousontmesaffaires.infrastructure.ConfigurationProperties;
import org.mongolink.Settings;
import org.mongolink.UpdateStrategies;

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
//        MongoCredential.createCredential()
        return Settings.defaultInstance()
                .withDatabase(new MongoClient(new ServerAddress(configurationProperties.databaseHost(), configurationProperties.databasePort()),
                        Lists.newArrayList()).getDatabase(configurationProperties.databaseName()))
                .withDefaultUpdateStrategy(UpdateStrategies.DIFF);
    }

    private final ConfigurationProperties configurationProperties;
}
