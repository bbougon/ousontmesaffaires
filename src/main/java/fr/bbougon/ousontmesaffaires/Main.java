package fr.bbougon.ousontmesaffaires;

import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(String[] args) {
        Configuration.ServerConfiguration serverConfiguration = Configuration.getServerConfiguration();
        UndertowJaxrsServer server = new UndertowJaxrsServer();
        server.deploy(new OuSontMesAffairesApplication());
        server.start(serverConfiguration.getSettings().getBuilder());
        LoggerFactory.getLogger(Main.class).info("Server started successfully on host 'localhost' and port {}", serverConfiguration.getSettings().getPort());
    }

}
