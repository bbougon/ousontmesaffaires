package fr.bbougon.ousontmesaffaires.infrastructure.module.transactional;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.command.CommandHandlers;
import fr.bbougon.ousontmesaffaires.command.CommandMiddlewares;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;

public class CommandBusSynchronous implements CommandBus {

    @Inject
    public CommandBusSynchronous(final CommandMiddlewares commandMiddlewares, CommandHandlers commandHandlers) {
        this.commandMiddlewares = commandMiddlewares;
        this.commandHandlers = commandHandlers;
    }

    @Override
    public <TResponse> CommandResponse<TResponse> send(final Command<TResponse> command) {
        Pair<TResponse, Event> response = commandHandlers.get(command.getClass()).execute(command);
        CommandResponse<TResponse> responseCommandResponse = new CommandResponse<>(response);
        commandMiddlewares.get(command.getClass())
                .ifPresent(commandMiddleware -> commandMiddleware.intercept(this, command, () -> response));
        return responseCommandResponse;
    }

    private CommandMiddlewares commandMiddlewares;
    private final CommandHandlers commandHandlers;
}
