package fr.bbougon.ousontmesaffaires;

import com.google.common.collect.Sets;
import com.google.inject.*;
import fr.bbougon.ousontmesaffaires.infrastructure.*;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongoRequestFilter;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongoResponseFilter;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationPath("/")
public class OuSontMesAffairesApplication extends Application {

    public OuSontMesAffairesApplication() {;
        injector = Guice.createInjector(Stage.DEVELOPMENT, new OuSontMesAffairesConfiguration());
    }

    @Override
    public Set<Object> getSingletons() {
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        Set<Object> instances = scanPackages().stream().map(injector::getInstance).collect(Collectors.toSet());
        instances.add(corsFilter);
        //instances.add(injector.getInstance(MongoRequestFilter.class));
        //instances.add(injector.getInstance(MongoResponseFilter.class));
        return instances;
    }

    private Set<Class<?>> scanPackages() {
        Set<Class<?>> result = Sets.newHashSet();
        String packageName = "fr.bbougon.ousontmesaffaires.web";
        new FastClasspathScanner(packageName).matchClassesWithAnnotation(Path.class, processor -> {
            if(!Modifier.isAbstract(processor.getModifiers()) && processor.getCanonicalName().startsWith(packageName)) {
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
