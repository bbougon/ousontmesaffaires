package fr.bbougon.ousontmesaffaires.infrastructure.security;

import org.junit.Before;
import org.junit.rules.ExternalResource;

public class WithSecurityService extends ExternalResource {

    @Override
    @Before
    public void before() {
        SecurityService.initialise(new SecurityService());
    }
}
