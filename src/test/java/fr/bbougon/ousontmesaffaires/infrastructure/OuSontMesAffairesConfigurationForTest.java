package fr.bbougon.ousontmesaffaires.infrastructure;

public class OuSontMesAffairesConfigurationForTest extends OuSontMesAffairesConfiguration {

    @Override
    void bindRemoteServices() {
        Services.initialise(new FakeServices());
    }

}
