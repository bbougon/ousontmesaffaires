package fr.bbougon.ousontmesaffaires.saga;

import fr.bbougon.ousontmesaffaires.command.CommandMiddleware;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.util.Set;
import java.util.function.Supplier;

public class SagaMiddleware implements CommandMiddleware<Saga> {

    @Inject
    public SagaMiddleware(Set<SagaHandler> sagaHandlers) {
        this.sagaHandlers = sagaHandlers;
    }

    @Override
    public <T> Pair<T, Event> intercept(final CommandBus bus, final Saga saga, final Supplier<Pair<T, Event>> supplier) {
        return Pair.of((T) sagaHandlers.stream()
                .filter(sagaHandler -> commandType(sagaHandler.getClass()).equals(saga.getClass()))
                .findFirst()
                .orElse(null)
                .run(bus, (Saga<T>) saga), null);
    }

    private <T> Class<T> commandType(final Class<?> sagaHandlerClass) {
        ParameterizedType type = (ParameterizedType) sagaHandlerClass.getGenericInterfaces()[0];
        return (Class<T>) type.getActualTypeArguments()[0];
    }


    private Set<SagaHandler> sagaHandlers;
}
