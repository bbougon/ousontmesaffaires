package fr.bbougon.ousontmesaffaires;

import fr.bbougon.ousontmesaffaires.repositories.*;
import io.undertow.Undertow;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.junit.rules.ExternalResource;
import org.mongolink.Settings;

public class WithEmbeddedServer extends ExternalResource {

    @Override
    public void before() throws Exception {
        loadConfiguration();
        start();
    }

    @Override
    public void after() {
        server.stop();
        FileRepositories.initialise(new DefaultFileRepositories());
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

    private void start() throws Exception {
        Configuration.ServerConfiguration configuration = Configuration.getServerConfiguration();
        server = new UndertowJaxrsServer();
        server.deploy(new OuSontMesAffairesApplication());
        Undertow.Builder serverConfiguration = Undertow.builder().addHttpListener(configuration.getPort(), "localhost");
        server.start(serverConfiguration);
    }

    private UndertowJaxrsServer server;
}
