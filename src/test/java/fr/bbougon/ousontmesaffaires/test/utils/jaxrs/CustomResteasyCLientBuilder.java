package fr.bbougon.ousontmesaffaires.test.utils.jaxrs;

import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ClientResponse;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.CompletionStageRxInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.RxInvoker;
import javax.ws.rs.core.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Locale;
import java.util.Map;

public class CustomResteasyCLientBuilder {

    public ResteasyClientBuilder getResteasyClientBuilder() {
        return new ResteasyClientBuilder() {
            @Override
            public ResteasyClient build() {
                return new ResteasyClient(null, null, false, null){
                    @Override
                    public ResteasyWebTarget target(final String uri) throws IllegalArgumentException, NullPointerException {
                        return new ResteasyWebTarget() {
                            @Override
                            public ResteasyClient getResteasyClient() {
                                return null;
                            }

                            @Override
                            public <T> T proxy(final Class<T> aClass) {
                                return null;
                            }

                            @Override
                            public <T> ProxyBuilder<T> proxyBuilder(final Class<T> aClass) {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget resolveTemplate(final String s, final Object o) throws NullPointerException {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget resolveTemplates(final Map<String, Object> map) throws NullPointerException {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget resolveTemplate(final String s, final Object o, final boolean b) throws NullPointerException {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget resolveTemplateFromEncoded(final String s, final Object o) throws NullPointerException {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget resolveTemplatesFromEncoded(final Map<String, Object> map) throws NullPointerException {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget resolveTemplates(final Map<String, Object> map, final boolean b) throws NullPointerException {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget path(final String s) throws NullPointerException {
                                return this;
                            }

                            @Override
                            public ResteasyWebTarget matrixParam(final String s, final Object... objects) throws NullPointerException {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget queryParam(final String s, final Object... objects) throws NullPointerException {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget queryParams(final MultivaluedMap<String, Object> multivaluedMap) throws IllegalArgumentException, NullPointerException {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget queryParamNoTemplate(final String s, final Object... objects) throws NullPointerException {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget queryParamsNoTemplate(final MultivaluedMap<String, Object> multivaluedMap) throws IllegalArgumentException, NullPointerException {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget path(final Class<?> aClass) throws IllegalArgumentException {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget path(final Method method) throws IllegalArgumentException {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget clone() {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget property(final String s, final Object o) {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget register(final Class<?> aClass) {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget register(final Class<?> aClass, final int i) {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget register(final Class<?> aClass, final Class<?>... classes) {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget register(final Class<?> aClass, final Map<Class<?>, Integer> map) {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget register(final Object o) {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget register(final Object o, final int i) {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget register(final Object o, final Class<?>... classes) {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget register(final Object o, final Map<Class<?>, Integer> map) {
                                return null;
                            }

                            @Override
                            public ResteasyWebTarget setChunked(final boolean b) {
                                return null;
                            }

                            @Override
                            public URI getUri() {
                                return null;
                            }

                            @Override
                            public UriBuilder getUriBuilder() {
                                return null;
                            }

                            @Override
                            public Invocation.Builder request() {
                                return new Invocation.Builder() {
                                    @Override
                                    public Invocation build(final String s) {
                                        return null;
                                    }

                                    @Override
                                    public Invocation build(final String s, final Entity<?> entity) {
                                        return null;
                                    }

                                    @Override
                                    public Invocation buildGet() {
                                        return null;
                                    }

                                    @Override
                                    public Invocation buildDelete() {
                                        return null;
                                    }

                                    @Override
                                    public Invocation buildPost(final Entity<?> entity) {
                                        return null;
                                    }

                                    @Override
                                    public Invocation buildPut(final Entity<?> entity) {
                                        return null;
                                    }

                                    @Override
                                    public AsyncInvoker async() {
                                        return null;
                                    }

                                    @Override
                                    public Invocation.Builder accept(final String... strings) {
                                        return null;
                                    }

                                    @Override
                                    public Invocation.Builder accept(final MediaType... mediaTypes) {
                                        return null;
                                    }

                                    @Override
                                    public Invocation.Builder acceptLanguage(final Locale... locales) {
                                        return null;
                                    }

                                    @Override
                                    public Invocation.Builder acceptLanguage(final String... strings) {
                                        return null;
                                    }

                                    @Override
                                    public Invocation.Builder acceptEncoding(final String... strings) {
                                        return null;
                                    }

                                    @Override
                                    public Invocation.Builder cookie(final Cookie cookie) {
                                        return null;
                                    }

                                    @Override
                                    public Invocation.Builder cookie(final String s, final String s1) {
                                        return null;
                                    }

                                    @Override
                                    public Invocation.Builder cacheControl(final CacheControl cacheControl) {
                                        return null;
                                    }

                                    @Override
                                    public Invocation.Builder header(final String s, final Object o) {
                                        return null;
                                    }

                                    @Override
                                    public Invocation.Builder headers(final MultivaluedMap<String, Object> multivaluedMap) {
                                        return null;
                                    }

                                    @Override
                                    public Invocation.Builder property(final String s, final Object o) {
                                        return null;
                                    }

                                    @Override
                                    public CompletionStageRxInvoker rx() {
                                        return null;
                                    }

                                    @Override
                                    public <T extends RxInvoker> T rx(final Class<T> aClass) {
                                        return null;
                                    }

                                    @Override
                                    public Response get() {
                                        return null;
                                    }

                                    @Override
                                    public <T> T get(final Class<T> aClass) {
                                        return null;
                                    }

                                    @Override
                                    public <T> T get(final GenericType<T> genericType) {
                                        return null;
                                    }

                                    @Override
                                    public Response put(final Entity<?> entity) {
                                        return null;
                                    }

                                    @Override
                                    public <T> T put(final Entity<?> entity, final Class<T> aClass) {
                                        return null;
                                    }

                                    @Override
                                    public <T> T put(final Entity<?> entity, final GenericType<T> genericType) {
                                        return null;
                                    }

                                    @Override
                                    public Response post(final Entity<?> entity) {
                                        if(CustomResteasyCLientBuilder.this.throwExceptionOnResponse) {
                                            throw new IllegalStateException();
                                        }
                                        return ClientResponse.ok().entity(new FileUtilsForTest("json/analyse-response.json").getContent()).build();
                                    }

                                    @Override
                                    public <T> T post(final Entity<?> entity, final Class<T> aClass) {
                                        return null;
                                    }

                                    @Override
                                    public <T> T post(final Entity<?> entity, final GenericType<T> genericType) {
                                        return null;
                                    }

                                    @Override
                                    public Response delete() {
                                        return null;
                                    }

                                    @Override
                                    public <T> T delete(final Class<T> aClass) {
                                        return null;
                                    }

                                    @Override
                                    public <T> T delete(final GenericType<T> genericType) {
                                        return null;
                                    }

                                    @Override
                                    public Response head() {
                                        return null;
                                    }

                                    @Override
                                    public Response options() {
                                        return null;
                                    }

                                    @Override
                                    public <T> T options(final Class<T> aClass) {
                                        return null;
                                    }

                                    @Override
                                    public <T> T options(final GenericType<T> genericType) {
                                        return null;
                                    }

                                    @Override
                                    public Response trace() {
                                        return null;
                                    }

                                    @Override
                                    public <T> T trace(final Class<T> aClass) {
                                        return null;
                                    }

                                    @Override
                                    public <T> T trace(final GenericType<T> genericType) {
                                        return null;
                                    }

                                    @Override
                                    public Response method(final String s) {
                                        return null;
                                    }

                                    @Override
                                    public <T> T method(final String s, final Class<T> aClass) {
                                        return null;
                                    }

                                    @Override
                                    public <T> T method(final String s, final GenericType<T> genericType) {
                                        return null;
                                    }

                                    @Override
                                    public Response method(final String s, final Entity<?> entity) {
                                        return null;
                                    }

                                    @Override
                                    public <T> T method(final String s, final Entity<?> entity, final Class<T> aClass) {
                                        return null;
                                    }

                                    @Override
                                    public <T> T method(final String s, final Entity<?> entity, final GenericType<T> genericType) {
                                        return null;
                                    }
                                };
                            }

                            @Override
                            public Invocation.Builder request(final String... strings) {
                                return null;
                            }

                            @Override
                            public Invocation.Builder request(final MediaType... mediaTypes) {
                                return null;
                            }

                            @Override
                            public Configuration getConfiguration() {
                                return null;
                            }
                        };
                    }
                };
            }
        };
    }

    public CustomResteasyCLientBuilder withExceptionOnResponseCall() {
        this.throwExceptionOnResponse = true;
        return this;
    }

    boolean throwExceptionOnResponse;
}
