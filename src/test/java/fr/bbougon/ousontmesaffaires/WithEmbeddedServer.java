package fr.bbougon.ousontmesaffaires;

import fr.bbougon.ousontmesaffaires.rules.WithFileRepositories;
import io.undertow.Undertow;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.junit.Rule;
import org.junit.rules.ExternalResource;

public class WithEmbeddedServer extends ExternalResource {

    @Rule
    public WithFileRepositories withFileRepositories = new WithFileRepositories();

    public WithEmbeddedServer() {
    }

    @Override
    public void before() {
        start();
    }

    @Override
    public void after() {
        server.stop();
    }

    private void start() {
        Configuration.ServerConfiguration configuration = Configuration.getServerConfiguration();
        server = new UndertowJaxrsServer();
        server.deploy(new OuSontMesAffairesApplicationForTest());
        Undertow.Builder serverConfiguration = Undertow.builder().addHttpListener(configuration.getSettings().getPort(), "localhost");
        server.start(serverConfiguration);
    }

    private UndertowJaxrsServer server;
}
