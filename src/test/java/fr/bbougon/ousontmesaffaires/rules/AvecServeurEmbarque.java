package fr.bbougon.ousontmesaffaires.rules;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.rules.ExternalResource;

public class AvecServeurEmbarque extends ExternalResource {

    @Override
    public void before() {
        server = new Server(8080);
        WebAppContext context = new WebAppContext();
        context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        context.setResourceBase("src/main/webapp");
        context.setContextPath("/");
        server.setHandler(context);
        start();
    }

    private void start() {
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void after() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AvecServeurEmbarque demarre() {
        return this;
    }

    public String getUrl() {
        return server.getURI().toString();
    }

    private Server server;
}
