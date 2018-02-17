package fr.bbougon.ousontmesaffaires.infrastructure;

import fr.bbougon.ousontmesaffaires.infrastructure.module.QRModuleForTest;

public class OuSontMesAffairesConfigurationForTest extends OuSontMesAffairesConfiguration {

    @Override
    void installQRModule() {
        install(new QRModuleForTest());
    }
}
