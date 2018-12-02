package fr.bbougon.ousontmesaffaires.infrastructure;

import fr.bbougon.ousontmesaffaires.infrastructure.nlp.RemoteNLPService;

public class RemoteServices extends Services{

    @Override
    protected NLPService getNlpService() {
        return new RemoteNLPService();
    }
}
