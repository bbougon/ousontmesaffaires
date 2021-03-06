package fr.bbougon.ousontmesaffaires.infrastructure;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.CommandMiddleware;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkModule;
import fr.bbougon.ousontmesaffaires.infrastructure.module.security.SecurityModule;
import fr.bbougon.ousontmesaffaires.infrastructure.module.transactional.CommandBusSynchronous;
import fr.bbougon.ousontmesaffaires.repositories.FileRepositories;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.mongo.MongoRepositories;
import fr.bbougon.ousontmesaffaires.saga.SagaHandler;
import fr.bbougon.ousontmesaffaires.saga.SagaMiddleware;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class OuSontMesAffairesConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        configureSagas();
        configureCommands();
        install(new MongolinkModule("fr.bbougon.ousontmesaffaires.repositories.mongo.mapping", FileRepositories.dataBaseConfiguration().get().getSettings()));
        install(new SecurityModule());
        bind(Repositories.class).to(MongoRepositories.class).in(Singleton.class);
        requestStaticInjection(Repositories.class);
        bindRemoteServices();
    }

    void bindRemoteServices() {
        bind(Services.class).to(RemoteServices.class).in(Singleton.class);
        requestStaticInjection(Services.class);
    }

    private void configureSagas() {
        Multibinder<SagaHandler> sagaHandlerMultibinder = Multibinder.newSetBinder(binder(), SagaHandler.class);
        scanPackageAndBind(SagaHandler.class, sagaHandlerMultibinder, "fr.bbougon.ousontmesaffaires.saga");
    }

    private void configureCommands() {
        Multibinder<CommandHandler> multibinder = Multibinder.newSetBinder(binder(), CommandHandler.class);
        scanPackageAndBind(CommandHandler.class, multibinder, "fr.bbougon.ousontmesaffaires.command");
        Multibinder<CommandMiddleware> multiMiddleware = Multibinder.newSetBinder(binder(), CommandMiddleware.class);
        multiMiddleware.addBinding().to(SagaMiddleware.class);
        bind(CommandBus.class).to(CommandBusSynchronous.class);
    }

    private static <T> void scanPackageAndBind(Class<T> type, Multibinder<T> multibinder, final String packageName) {
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
