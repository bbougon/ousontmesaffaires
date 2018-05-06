package fr.bbougon.ousontmesaffaires.rules;

import fr.bbougon.ousontmesaffaires.Configuration;
import fr.bbougon.ousontmesaffaires.infrastructure.ConfigurationProperties;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecuritySettings;
import fr.bbougon.ousontmesaffaires.repositories.DefaultFileRepositories;
import fr.bbougon.ousontmesaffaires.repositories.FileRepositories;
import fr.bbougon.ousontmesaffaires.repositories.FileRepository;
import io.undertow.Undertow;
import org.junit.After;
import org.junit.rules.ExternalResource;
import org.mongolink.Settings;

public class WithFileRepositories extends ExternalResource {

    public WithFileRepositories() {
        FileRepositories.initialise(new FileRepositories() {
            @Override
            public FileRepository<Configuration.ServerConfiguration> getServerConfiguration() {
                return () -> (Configuration.ServerConfiguration) () -> new Configuration.ServerSettings() {
                    @Override
                    public Undertow.Builder getBuilder() {
                        return Undertow.builder().addHttpListener(getPort(), "localhost");
                    }

                    @Override
                    public int getPort() {
                        return 17000;
                    }
                };
            }

            @Override
            protected FileRepository<Configuration.DataBaseConfiguration> getDataBaseConfiguration() {
                return () -> (Configuration.DataBaseConfiguration) Settings::defaultInstance;
            }

            @Override
            protected FileRepository<Configuration.SecurityConfiguration> getSecurityConfiguration() {
                return () -> (Configuration.SecurityConfiguration) () -> new SecuritySettings("abcd", "12345");
            }
        });
    }

    @Override
    @After
    public void after() {
        super.after();
        FileRepositories.initialise(new DefaultFileRepositories(new ConfigurationProperties()));
    }
}
