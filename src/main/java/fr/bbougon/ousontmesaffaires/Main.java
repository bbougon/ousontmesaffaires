package fr.bbougon.ousontmesaffaires;

import io.undertow.Undertow;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        Configuration.ServerConfiguration configuration = Configuration.getServerConfiguration();
        UndertowJaxrsServer server = new UndertowJaxrsServer();
        server.deploy(new OuSontMesAffairesApplication());
        Undertow.Builder serverConfiguration = Undertow.builder().addHttpListener(configuration.getPort(), InetAddress.getLocalHost().getHostAddress());
        server.start(serverConfiguration);
        LoggerFactory.getLogger(Main.class).info("Server started successfully on host 'localhost' and port {}", configuration.getPort());
    }
}
