package fr.bbougon.ousontmesaffaires;

import com.google.common.collect.Sets;
import com.google.inject.*;
import fr.bbougon.ousontmesaffaires.infrastructure.OuSontMesAffairesConfiguration;
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

    OuSontMesAffairesApplication() {
        injector = Guice.createInjector(Stage.DEVELOPMENT, new OuSontMesAffairesConfiguration());
    }

    @Override
    public Set<Object> getSingletons() {
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        corsFilter.setExposedHeaders("Cache-Control, Content-Language, Content-Type, Expires, Last-Modified, Pragma, Location");
        corsFilter.setAllowedHeaders("Location, Content-Type");
        Set<Object> instances = scanPackages().stream().map(injector::getInstance).collect(Collectors.toSet());
        instances.add(corsFilter);
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

    private final Injector injector;
}
