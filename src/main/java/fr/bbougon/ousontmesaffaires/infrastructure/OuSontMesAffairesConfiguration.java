package fr.bbougon.ousontmesaffaires.infrastructure;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkModule;
import fr.bbougon.ousontmesaffaires.infrastructure.module.security.SecurityModule;
import fr.bbougon.ousontmesaffaires.infrastructure.module.transactional.TransactionalMiddleware;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGeneratorEngine;
import fr.bbougon.ousontmesaffaires.repositories.FileRepositories;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.mongo.MongoRepositories;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class OuSontMesAffairesConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        installMiddleWare();
        install(new MongolinkModule("fr.bbougon.ousontmesaffaires.repositories.mongo.mapping", FileRepositories.dataBaseConfiguration().get().getSettings()));
        install(new SecurityModule());
        bindQRGenerator();
        bind(Repositories.class).to(MongoRepositories.class).in(Singleton.class);
        requestStaticInjection(Repositories.class);
    }

    void bindQRGenerator() {
        bind(QRGenerator.class).to(QRGeneratorEngine.class);
    }

    private void installMiddleWare() {
        Multibinder<CommandHandler> multibinder = Multibinder.newSetBinder(binder(), CommandHandler.class);
        scanPackageAndBind(CommandHandler.class, multibinder);
        bind(CommandBus.class).to(TransactionalMiddleware.class);
    }

    private static void scanPackageAndBind(Class<CommandHandler> type, Multibinder<CommandHandler> multibinder) {
        String packageName = "fr.bbougon.ousontmesaffaires.command";
        try (ScanResult scanResult = new ClassGraph()
                .whitelistPackages(packageName)
                .scan()) {
            scanResult.getClassesImplementing(type.getCanonicalName())
                    .stream()
                    .map(classInfo -> classInfo.loadClass(type))
                    .forEach(classInfo -> multibinder.addBinding().to(classInfo));
        }
    }
}
