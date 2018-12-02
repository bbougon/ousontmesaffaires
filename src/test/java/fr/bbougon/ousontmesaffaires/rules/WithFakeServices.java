package fr.bbougon.ousontmesaffaires.rules;

import fr.bbougon.ousontmesaffaires.infrastructure.FakeServices;
import fr.bbougon.ousontmesaffaires.infrastructure.Services;
import org.junit.After;
import org.junit.rules.ExternalResource;

public class WithFakeServices extends ExternalResource {

    private final FakeServices services = new FakeServices();

    public FakeServices getFakeService() {
        return services;
    }

    public WithFakeServices() {
        Services.initialise(services);
    }

    @Override
    @After
    public void after() {
        super.after();
        Services.initialise(null);
    }
}
