package fr.bbougon.ousontmesaffaires;

import fr.bbougon.ousontmesaffaires.Configuration.ServerConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration.ClassList;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

class EmbeddedServer {

    private EmbeddedServer() {
    }

    static String getUrl() {
        return ServerHolder.INSTANCE.server.getURI().toString();
    }

    static void start(final ServerConfiguration serverConfiguration) {
        ServerHolder.INSTANCE.setConfiguration(serverConfiguration);
        ServerHolder.INSTANCE.start();
    }

    private void start() {
        try {
            server = new Server(new InetSocketAddress(configuration.getPort()));
            ClassList classlist = ClassList.setServerDefault(server);
            classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");
            WebAppContext context = new WebAppContext();
            context.setDescriptor(configuration.getDescriptor());
            context.setResourceBase(".");
            context.setContextPath("/");
            server.setHandler(context);
            server.start();
        } catch (Exception e) {
            LoggerFactory.getLogger(EmbeddedServer.class).error("Impossible to start server with descriptor file '{}' on port '{}'", configuration.getDescriptor(), configuration.getPort());
        }
    }

    private void setConfiguration(final ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    static void stop() {
        try {
            ServerHolder.INSTANCE.server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ServerHolder {
        static final EmbeddedServer INSTANCE = new EmbeddedServer();
    }

    private Server server;
    private ServerConfiguration configuration;
}
