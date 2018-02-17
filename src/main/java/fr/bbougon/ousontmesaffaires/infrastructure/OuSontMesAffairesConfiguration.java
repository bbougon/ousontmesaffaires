package fr.bbougon.ousontmesaffaires.infrastructure;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.module.QRModule;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkModule;
import fr.bbougon.ousontmesaffaires.infrastructure.module.transactional.TransactionalMiddleware;
import fr.bbougon.ousontmesaffaires.repositories.FileRepositories;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.mongo.MongoRepositories;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.lang.reflect.Modifier;


public class OuSontMesAffairesConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        installMiddleWare();
        install(new MongolinkModule("fr.bbougon.ousontmesaffaires.repositories.mongo.mapping", FileRepositories.dataBaseConfiguration().get().getSettings()));
        installQRModule();
        bind(Repositories.class).to(MongoRepositories.class).in(Singleton.class);
        requestStaticInjection(Repositories.class);
    }

    void installQRModule() {
        install(new QRModule());
    }

    private void installMiddleWare() {
        Multibinder<CommandHandler> multibinder = Multibinder.newSetBinder(binder(), CommandHandler.class);
        scanPackageAndBind(CommandHandler.class, multibinder);
        bind(CommandBus.class).to(TransactionalMiddleware.class);
    }

    private static void scanPackageAndBind(Class<CommandHandler> type, Multibinder<CommandHandler> multibinder) {
        String packageName = "fr.bbougon.ousontmesaffaires.command";
        new FastClasspathScanner(packageName, type.getPackage().getName())
                .matchClassesImplementing(type, foundType -> {
                    if (!Modifier.isAbstract(foundType.getModifiers()) && foundType.getCanonicalName().startsWith(packageName)) {
                        multibinder.addBinding().to(foundType);
                    }
                }).scan();
    }
}
