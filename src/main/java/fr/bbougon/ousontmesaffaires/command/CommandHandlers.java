package fr.bbougon.ousontmesaffaires.command;

import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

public class CommandHandlers {

    @Inject
    public CommandHandlers(Set<CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers;
    }

    public CommandHandler get(final Class<? extends Command> commandClass) {
        return commandHandlers.stream()
                .filter(commandHandler -> commandType(commandHandler.getClass()).equals(commandClass))
                .findFirst()
                .orElseThrow(UnknownCommandException::new);
    }

    private <T> Class<T> commandType(final Class<?> commandHandlerClass) {
        ParameterizedType type = (ParameterizedType) commandHandlerClass.getGenericInterfaces()[0];
        return (Class<T>) type.getActualTypeArguments()[0];
    }

    private final Set<CommandHandler> commandHandlers;

}
