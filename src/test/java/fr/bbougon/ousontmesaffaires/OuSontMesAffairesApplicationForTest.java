package fr.bbougon.ousontmesaffaires;

import com.google.inject.Injector;
import fr.bbougon.ousontmesaffaires.infrastructure.OuSontMesAffairesConfigurationForTest;
import fr.bbougon.ousontmesaffaires.infrastructure.OuSontMesAffairesConfiguration;

public class OuSontMesAffairesApplicationForTest extends OuSontMesAffairesApplication {

    @Override
    OuSontMesAffairesConfiguration getConfiguration() {
        return new OuSontMesAffairesConfigurationForTest();
    }

    public Injector getInjector() {
        return injector;
    }

}
