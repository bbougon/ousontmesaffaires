package fr.bbougon.ousontmesaffaires.command;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

public class CommandMiddlewares {

    @Inject
    public CommandMiddlewares(final Set<CommandMiddleware> commandMiddlewares) {
        this.commandMiddlewares = commandMiddlewares;
    }

    public Optional<CommandMiddleware> get(final Class<? extends Command> aClass) {
        return commandMiddlewares.stream()
                .filter(commandMiddleware -> commandMiddleware.getClass().equals(aClass))
                .findFirst();
    }

    private Set<CommandMiddleware> commandMiddlewares;
}
