package fr.bbougon.ousontmesaffaires;

import com.fasterxml.jackson.jaxrs.cfg.JaxRSFeature;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import fr.bbougon.ousontmesaffaires.infrastructure.OuSontMesAffairesConfiguration;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationPath("/")
public class OuSontMesAffairesApplication extends Application {

    OuSontMesAffairesApplication() {
        injector = Guice.createInjector(Stage.DEVELOPMENT, getConfiguration());
    }

    OuSontMesAffairesConfiguration getConfiguration() {
        return new OuSontMesAffairesConfiguration();
    }

    @Override
    public Set<Object> getSingletons() {
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        corsFilter.setExposedHeaders("Cache-Control, Content-Language, Content-Type, Expires, Last-Modified, Pragma, Location");
        corsFilter.setAllowedHeaders("Location, Content-Type");
        Set<Object> instances = scanPackages().stream().map(injector::getInstance).collect(Collectors.toSet());
        instances.add(corsFilter);
        instances.add(jsonProviders(instances));
        return instances;
    }

    private ResteasyJackson2Provider jsonProviders(final Set<Object> instances) {
        ResteasyJackson2Provider jacksonJsonProvider = new ResteasyJackson2Provider();
        jacksonJsonProvider.configure(JaxRSFeature.ALLOW_EMPTY_INPUT, true);
        return jacksonJsonProvider;
    }

    private Set<Class<?>> scanPackages() {
        Set<Class<?>> result = scanPackages("fr.bbougon.ousontmesaffaires.web", Path.class);
        result.addAll(scanPackages("fr.bbougon.ousontmesaffaires.web.mappers", Provider.class));
        return result;
    }

    private Set<Class<?>> scanPackages(final String resourcesPackageName, final Class<?> annotation) {
        try (ScanResult scanResult = new ClassGraph()
                .whitelistPackages(resourcesPackageName)
                .enableAllInfo()
                .scan()) {
            return scanResult.getClassesWithAnnotation(annotation.getCanonicalName())
                    .stream()
                    .map(ClassInfo::loadClass)
                    .collect(Collectors.toSet());
        }
    }

    private final Injector injector;
}
