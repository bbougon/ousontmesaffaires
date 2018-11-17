package fr.bbougon.ousontmesaffaires.command;

import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;

public interface CommandMiddleware<Tcommand extends Command> {

    <T> Pair<T, Event> intercept(CommandBus bus, Tcommand tcommand, Supplier<Pair<T, Event>> supplier);
}
