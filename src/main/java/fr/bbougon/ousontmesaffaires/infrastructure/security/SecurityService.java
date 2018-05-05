package fr.bbougon.ousontmesaffaires.infrastructure.security;

import javax.inject.Inject;

public class SecurityService {

    public static void initialise(SecurityService securityService) {
        SecurityService.securityService = securityService;
    }

    public static Encryptor sha1() {
        return securityService.getSha1();
    }

    private Encryptor getSha1() {
        return new Sha1Encryptor();
    }

    @Inject
    private static SecurityService securityService;
}
