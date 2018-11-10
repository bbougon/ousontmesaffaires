package fr.bbougon.ousontmesaffaires.saga;

import fr.bbougon.ousontmesaffaires.command.CommandMiddleware;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;

public class SagaMiddleware implements CommandMiddleware<Saga> {
    @Override
    public <T> Pair<T, Event> intercept(final CommandBus bus, final Saga command, final Supplier<Pair<T, Event>> supplier) {
        return null;
    }
}
