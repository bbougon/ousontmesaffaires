package fr.bbougon.ousontmesaffaires.web;

import fr.bbougon.ousontmesaffaires.entrepot.Entrepots;
import fr.bbougon.ousontmesaffaires.entrepot.MongoConfiguration;
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
