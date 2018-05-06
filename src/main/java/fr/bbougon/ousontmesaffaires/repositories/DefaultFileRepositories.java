package fr.bbougon.ousontmesaffaires.repositories;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import fr.bbougon.ousontmesaffaires.Configuration;
import fr.bbougon.ousontmesaffaires.Configuration.DataBaseConfiguration;
import fr.bbougon.ousontmesaffaires.Configuration.SecurityConfiguration;
import fr.bbougon.ousontmesaffaires.Configuration.ServerConfiguration;
import fr.bbougon.ousontmesaffaires.Main;
import fr.bbougon.ousontmesaffaires.infrastructure.ConfigurationProperties;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecuritySettings;
import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import org.mongolink.Settings;
import org.mongolink.UpdateStrategies;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.net.InetAddress;
import java.util.List;

public class DefaultFileRepositories extends FileRepositories {

    public DefaultFileRepositories(final ConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @Override
    public FileRepository<ServerConfiguration> getServerConfiguration() {
        return () -> (ServerConfiguration) this::getServerSettings;
    }

    @Override
    protected FileRepository<DataBaseConfiguration> getDataBaseConfiguration() {
        return () -> (DataBaseConfiguration) this::getSettings;
    }

    @Override
    protected FileRepository<SecurityConfiguration> getSecurityConfiguration() {
        return () -> (SecurityConfiguration) this::getSecuritySettings;
    }

    private SecuritySettings getSecuritySettings() {
        return new SecuritySettings(configurationProperties.thirdPartServiceSecret());
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

    private Configuration.ServerSettings getServerSettings() {
        return new Configuration.ServerSettings() {
            @Override
            public Builder getBuilder() {
                try {
                    if (configurationProperties.secureServer()) {
                        return Undertow.builder().addHttpsListener(getPort(), InetAddress.getLocalHost().getHostAddress(), SSLContext.getDefault());
                    }
                    return Undertow.builder().addHttpListener(getPort(), InetAddress.getLocalHost().getHostAddress());
                } catch (Exception e) {
                    LoggerFactory.getLogger(Main.class).info("Server started with errors on host 'localhost' and port {}: {}", getPort(), e.getMessage());
                    return null;
                }
            }

            @Override
            public int getPort() {
                return configurationProperties.serverPort();
            }
        };
    }

    private final ConfigurationProperties configurationProperties;
}
