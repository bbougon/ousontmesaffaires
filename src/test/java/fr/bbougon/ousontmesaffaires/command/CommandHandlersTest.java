package fr.bbougon.ousontmesaffaires.command;

import com.google.common.collect.Sets;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class CommandHandlersTest {

    @Test
    public void handleExceptions() {
        try {
            CommandHandlers commandHandlers = new CommandHandlers(Sets.newHashSet());
            commandHandlers.get(UnknownCommand.class);
            failBecauseExceptionWasNotThrown(UnknownCommandException.class);
        } catch (UnknownCommandException e) {
            assertThat(e.getMessage()).isEqualTo("Command 'UnknownCommand' is unknown!");
        }
    }

    private class UnknownCommand implements Command {
    }
}