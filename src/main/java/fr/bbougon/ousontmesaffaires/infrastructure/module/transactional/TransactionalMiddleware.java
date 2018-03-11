package fr.bbougon.ousontmesaffaires.infrastructure.module.transactional;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.command.CommandHandlers;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkTransaction;

import javax.inject.Inject;

public class TransactionalMiddleware implements CommandBus {

    @Inject
    public TransactionalMiddleware(CommandHandlers commandHandlers) {
        this.commandHandlers = commandHandlers;
    }

    @MongolinkTransaction
    @Override
    public <TResponse> CommandResponse send(final Command<TResponse> command) {
        return new CommandResponse<TResponse>(commandHandlers.get(command.getClass()).execute(command));
    }

    private final CommandHandlers commandHandlers;
}
