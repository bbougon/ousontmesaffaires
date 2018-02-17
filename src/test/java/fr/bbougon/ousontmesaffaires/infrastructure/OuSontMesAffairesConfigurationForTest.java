package fr.bbougon.ousontmesaffaires.infrastructure;

import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGeneratorForTest;

public class OuSontMesAffairesConfigurationForTest extends OuSontMesAffairesConfiguration {

    @Override
    void bindQRGenerator() {
        bind(QRGenerator.class).to(QRGeneratorForTest.class);
    }
}
