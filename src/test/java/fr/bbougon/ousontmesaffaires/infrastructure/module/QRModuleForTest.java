package fr.bbougon.ousontmesaffaires.infrastructure.module;

import com.google.inject.AbstractModule;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGeneratorForTest;

public class QRModuleForTest extends AbstractModule {
    @Override
    protected void configure() {
        bind(QRGenerator.class).to(QRGeneratorForTest.class);
    }
}
