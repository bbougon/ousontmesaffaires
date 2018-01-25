package fr.bbougon.ousontmesaffaires;

import com.google.common.collect.Sets;
import com.google.inject.*;
import com.google.inject.util.Modules;
import fr.bbougon.ousontmesaffaires.infrastructure.module.QRModule;
import fr.bbougon.ousontmesaffaires.infrastructure.module.QRModuleForTest;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkModule;
import fr.bbougon.ousontmesaffaires.repositories.FileRepositories;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.mongo.MongoRepositories;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationPath("/")
public class OuSontMesAffairesApplicationForTest extends Application {

    public OuSontMesAffairesApplicationForTest() {
        injector = Guice.createInjector(Modules.override(new QRModule()).with(new QRModuleForTest()), new AbstractModule() {
            @Override
            protected void configure() {
                install(new MongolinkModule("fr.bbougon.ousontmesaffaires.repositories.mongo.mapping", FileRepositories.dataBaseConfiguration().get().getSettings()));
                install(new QRModuleForTest());
                bind(Repositories.class).to(MongoRepositories.class).in(Singleton.class);
                requestStaticInjection(Repositories.class);
            }
        });
    }


    @Override
    public Set<Object> getSingletons() {
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        Set<Object> instances = scanPackages().stream().map(injector::getInstance).collect(Collectors.toSet());
        instances.add(corsFilter);
        return instances;
    }

    private Set<Class<?>> scanPackages() {
        Set<Class<?>> result = Sets.newHashSet();
        String packageName = "fr.bbougon.ousontmesaffaires.web";
        new FastClasspathScanner(packageName).matchClassesWithAnnotation(Path.class, processor -> {
            if (!Modifier.isAbstract(processor.getModifiers()) && processor.getCanonicalName().startsWith(packageName)) {
                result.add(processor);
            }
        }).scan();
        return result;
    }

    public Injector getInjector() {
        return injector;
    }

    private final Injector injector;
}
