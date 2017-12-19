package fr.bbougon.ousontmesaffaires;

import fr.bbougon.ousontmesaffaires.Configuration.ConfigurationServeur;
import org.eclipse.jetty.server.Server;
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

    static void démarre(final ConfigurationServeur configurationServeur) {
        ServeurHolder.INSTANCE.setConfiguration(configurationServeur);
        ServeurHolder.INSTANCE.démarre();
    }

    private void démarre() {
        try {
            serveur = new Server(new InetSocketAddress(configuration.getPort()));
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

    private void setConfiguration(final ConfigurationServeur configuration) {
        this.configuration = configuration;
    }

    private static class ServeurHolder {
        static final Serveur INSTANCE = new Serveur();
    }

    private Server serveur;
    private ConfigurationServeur configuration;
}
