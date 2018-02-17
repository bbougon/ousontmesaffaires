package fr.bbougon.ousontmesaffaires.infrastructure.bus;

import fr.bbougon.ousontmesaffaires.command.Command;

public interface CommandBus {
    <TResponse> CommandResponse send(Command<TResponse> command);
}
