package fr.bbougon.ousontmesaffaires.infrastructure.module.transactional;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.command.CommandHandlers;
import fr.bbougon.ousontmesaffaires.command.CommandMiddleware;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.util.Set;
import java.util.function.Supplier;

public class CommandBusSynchronous implements CommandBus {

    @Inject
    public CommandBusSynchronous(final Set<CommandMiddleware> commandMiddlewares, final CommandHandlers commandHandlers) {
        this.commandMiddlewares = Sets.newHashSet(commandMiddlewares);
        this.commandMiddlewares.add(new InvokeCommandMiddleware(commandHandlers));
    }

    @Override
    public <TResponse> CommandResponse<TResponse> send(final Command<TResponse> command) {
        return new CommandResponse(commandMiddlewares.stream()
                .filter(commandMiddleware -> commandType(commandMiddleware.getClass()).equals(command.getClass().getInterfaces()[0]))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("No middleware found for command '%s'", command.getClass().getSimpleName())))
                .intercept(this, command, () -> ""));
    }

    private <T> Class<T> commandType(final Class<?> middleWare) {
        ParameterizedType type = (ParameterizedType) middleWare.getGenericInterfaces()[0];
        return (Class<T>) type.getActualTypeArguments()[0];
    }

    private class InvokeCommandMiddleware implements CommandMiddleware<Command> {
        InvokeCommandMiddleware(final CommandHandlers commandHandlers) {
            this.commandHandlers = commandHandlers;
        }

        @Override
        public <T> Pair<T, Event> intercept(final CommandBus bus, final Command command, final Supplier<Pair<T, Event>> supplier) {
            return commandHandlers.get(command.getClass()).execute(command);
        }

        private CommandHandlers commandHandlers;
    }

    private final Set<CommandMiddleware> commandMiddlewares;
}
