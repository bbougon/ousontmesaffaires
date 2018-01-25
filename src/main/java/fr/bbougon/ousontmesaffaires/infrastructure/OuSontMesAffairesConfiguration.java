package fr.bbougon.ousontmesaffaires.infrastructure;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import fr.bbougon.ousontmesaffaires.infrastructure.module.QRModule;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkModule;
import fr.bbougon.ousontmesaffaires.repositories.FileRepositories;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.mongo.MongoRepositories;


public class OuSontMesAffairesConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        install(new MongolinkModule("fr.bbougon.ousontmesaffaires.repositories.mongo.mapping", FileRepositories.dataBaseConfiguration().get().getSettings()));
        install(new QRModule());
        bind(Repositories.class).to(MongoRepositories.class).in(Singleton.class);
        requestStaticInjection(Repositories.class);
    }
}
