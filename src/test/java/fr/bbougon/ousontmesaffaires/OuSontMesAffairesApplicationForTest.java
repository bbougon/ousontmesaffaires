package fr.bbougon.ousontmesaffaires;

import fr.bbougon.ousontmesaffaires.infrastructure.OuSontMesAffairesConfiguration;
import fr.bbougon.ousontmesaffaires.infrastructure.OuSontMesAffairesConfigurationForTest;

public class OuSontMesAffairesApplicationForTest extends OuSontMesAffairesApplication {

    @Override
    OuSontMesAffairesConfiguration getConfiguration() {
        return new OuSontMesAffairesConfigurationForTest();
    }

}
