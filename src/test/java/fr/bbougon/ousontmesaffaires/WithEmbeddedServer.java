package fr.bbougon.ousontmesaffaires;

import fr.bbougon.ousontmesaffaires.infrastructure.ConfigurationProperties;
import fr.bbougon.ousontmesaffaires.repositories.DefaultFileRepositories;
import fr.bbougon.ousontmesaffaires.repositories.FileRepositories;
import fr.bbougon.ousontmesaffaires.repositories.FileRepository;
import io.undertow.Undertow;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.junit.rules.ExternalResource;
import org.mongolink.Settings;

public class WithEmbeddedServer extends ExternalResource {

    public WithEmbeddedServer(final boolean withQrCode) {
        this.withQrCode = withQrCode;
    }

    public WithEmbeddedServer() {
    }

    @Override
    public void before() {
        loadConfiguration();
        start();
    }

    @Override
    public void after() {
        server.stop();
        FileRepositories.initialise(new DefaultFileRepositories(new ConfigurationProperties()));
    }

    private void loadConfiguration() {
        FileRepositories.initialise(new FileRepositories() {
            @Override
            public FileRepository<Configuration.ServerConfiguration> getServerConfiguration() {
                return () -> (Configuration.ServerConfiguration) () -> 17000;
            }

            @Override
            protected FileRepository<Configuration.DataBaseConfiguration> getDataBaseConfiguration() {
                return () -> (Configuration.DataBaseConfiguration) Settings::defaultInstance;
            }
        });
    }

    private void start() {
        Configuration.ServerConfiguration configuration = Configuration.getServerConfiguration();
        server = new UndertowJaxrsServer();
        OuSontMesAffairesApplication application;
        if(!withQrCode) {
            application = new OuSontMesAffairesApplicationForTest();
        } else {
            application = new OuSontMesAffairesApplication();
        }
        server.deploy(application);
        Undertow.Builder serverConfiguration = Undertow.builder().addHttpListener(configuration.getPort(), "localhost");
        server.start(serverConfiguration);
    }

    private boolean withQrCode;

    private UndertowJaxrsServer server;
}
