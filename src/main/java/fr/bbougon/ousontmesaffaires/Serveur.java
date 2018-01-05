package fr.bbougon.ousontmesaffaires;

import fr.bbougon.ousontmesaffaires.Configuration.ServerConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration.ClassList;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.InetSocketAddress;

public class Serveur {

    private Serveur() {
    }

    public static void stop() {
        try {
            ServeurHolder.INSTANCE.serveur.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUrl() {
        return ServeurHolder.INSTANCE.serveur.getURI().toString();
    }

    static void démarre(final ServerConfiguration serverConfiguration) {
        ServeurHolder.INSTANCE.setConfiguration(serverConfiguration);
        ServeurHolder.INSTANCE.démarre();
    }

    private void démarre() {
        try {
            serveur = new Server(new InetSocketAddress(configuration.getPort()));
            ClassList classlist = ClassList.setServerDefault(serveur);
            classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");
            WebAppContext context = new WebAppContext();
            context.setDescriptor(configuration.getDescriptor());
            context.setResourceBase(".");
            context.setContextPath("/");
            serveur.setHandler(context);
            serveur.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setConfiguration(final ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    private static class ServeurHolder {
        static final Serveur INSTANCE = new Serveur();
    }

    private Server serveur;
    private ServerConfiguration configuration;
}
