package fr.bbougon.ousontmesaffaires.infrastructure;

import javax.inject.Inject;

public abstract class Services {

    public static void initialise(final Services services) {
        Services.services = services;
    }

    public static NLPService nlpService() {
        return services.getNlpService();
    }

    protected abstract NLPService getNlpService();

    @Inject
    private static Services services;
}
