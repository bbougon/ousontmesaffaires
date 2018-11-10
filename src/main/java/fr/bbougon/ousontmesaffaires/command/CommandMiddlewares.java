package fr.bbougon.ousontmesaffaires.command;

import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.Set;

public class CommandMiddlewares {

    @Inject
    public CommandMiddlewares(final Set<CommandMiddleware> commandMiddlewares) {
        this.commandMiddlewares = commandMiddlewares;
    }

    public Optional<CommandMiddleware> get(final Class<? extends Command> aClass) {
        return commandMiddlewares.stream()
                .filter(commandMiddleware -> commandType(commandMiddleware.getClass()).equals(aClass))
                .findFirst();
    }

    private <T> Class<T> commandType(final Class<?> commandHandlerClass) {
        ParameterizedType type = (ParameterizedType) commandHandlerClass.getGenericInterfaces()[0];
        return (Class<T>) type.getActualTypeArguments()[0];
    }

    private Set<CommandMiddleware> commandMiddlewares;
}
