package fr.bbougon.ousontmesaffaires.infrastructure;

import fr.bbougon.ousontmesaffaires.infrastructure.nlp.RemoteNLPService;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

public class RemoteServices extends Services {

    @Override
    protected NLPService getNlpService() {
        return new RemoteNLPService(new ResteasyClientBuilder());
    }
}
