package fr.bbougon.ousontmesaffaires.entrepot;

import fr.bbougon.ousontmesaffaires.entrepot.mongo.EntrepotsMongos;
import org.mongolink.MongoSession;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class ContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        MongoSession session = MongoConfiguration.startSession();
        Entrepots.initialise(new EntrepotsMongos(session));
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        MongoConfiguration.stopSession();
    }

}
