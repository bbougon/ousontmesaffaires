package fr.bbougon.ousontmesaffaires;

import fr.bbougon.ousontmesaffaires.Configuration.ServerConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration.ClassList;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.InetSocketAddress;

public class EmbeddedServer {

    private EmbeddedServer() {
    }

    public static String getUrl() {
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
            throw new RuntimeException(e);
        }
    }

    private void setConfiguration(final ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    private static class ServerHolder {
        static final EmbeddedServer INSTANCE = new EmbeddedServer();
    }

    private Server server;
    private ServerConfiguration configuration;
}
