package fr.bbougon.ousontmesaffaires.command;

import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;

public interface CommandMiddleware<TCommand extends Command> {

    <T> Pair<T, Event> intercept(CommandBus bus, TCommand command, Supplier<Pair<T, Event>> supplier);
}
