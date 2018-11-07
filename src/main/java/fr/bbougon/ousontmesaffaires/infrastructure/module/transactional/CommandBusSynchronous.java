package fr.bbougon.ousontmesaffaires.infrastructure.module.transactional;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.command.CommandHandlers;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;

import javax.inject.Inject;

public class CommandBusSynchronous implements CommandBus {

    @Inject
    public CommandBusSynchronous(CommandHandlers commandHandlers) {
        this.commandHandlers = commandHandlers;
    }

    @Override
    public <TResponse> CommandResponse<TResponse> send(final Command<TResponse> command) {
        return new CommandResponse<TResponse>(commandHandlers.get(command.getClass()).execute(command));
    }

    private final CommandHandlers commandHandlers;
}
