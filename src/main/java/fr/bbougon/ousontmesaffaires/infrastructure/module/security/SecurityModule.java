package fr.bbougon.ousontmesaffaires.infrastructure.module.security;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;

public class SecurityModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SecurityService.class).in(Singleton.class);
        requestStaticInjection(SecurityService.class);
    }

}
