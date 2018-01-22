package fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;

public class MongolinkTransactionInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        try {
            mongolinkSessionManager.start();
            return methodInvocation.proceed();
        } finally {
            mongolinkSessionManager.stop();
        }
    }

    @Inject
    private MongolinkSessionManager mongolinkSessionManager;

}
