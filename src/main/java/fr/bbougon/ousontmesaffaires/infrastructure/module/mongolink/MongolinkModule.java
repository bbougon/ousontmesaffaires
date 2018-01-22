package fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink;

import com.google.common.base.Preconditions;
import com.google.inject.*;
import com.google.inject.matcher.AbstractMatcher;
import org.mongolink.Settings;

import java.lang.reflect.Method;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

public class MongolinkModule extends AbstractModule {

    private static final class TransactionMethodMatcher extends AbstractMatcher<Method> {
        @Override
        public boolean matches(final Method method) {
            return method.isAnnotationPresent(MongolinkTransaction.class) && !method.isSynthetic();
        }
    }

    public MongolinkModule(final String mappingPackage, final Settings settings) {
        Preconditions.checkArgument(mappingPackage != null && !mappingPackage.isEmpty(), "mapping package cannot be null or empty.");
        Preconditions.checkArgument(settings != null, "Mongo settings cannot be null.");
        this.mappingPackage = mappingPackage;
        this.settings = settings;
    }

    @Override
    protected void configure() {
        install(new MongoDbModule(mappingPackage, settings));
        bind(MongoRequestFilter.class).in(Singleton.class);
        bind(MongoResponseFilter.class).in(Singleton.class);
        MongolinkTransactionInterceptor mongolinkTransactionInterceptor = new MongolinkTransactionInterceptor();
        requestInjection(mongolinkTransactionInterceptor);
        bindInterceptor(annotatedWith(MongolinkTransaction.class), new TransactionMethodMatcher(), mongolinkTransactionInterceptor);
        bindInterceptor(any(), annotatedWith(MongolinkTransaction.class), mongolinkTransactionInterceptor);
    }

    @Provides
    public Settings settings() {
        return settings;
    }

    private final String mappingPackage;
    private final Settings settings;
}
