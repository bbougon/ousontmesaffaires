package fr.bbougon.ousontmesaffaires.infrastructure.module.transactional;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.CommandHandlers;
import fr.bbougon.ousontmesaffaires.command.CommandMiddleware;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class CommandBusSynchronousTest {

    @Test
    public void canSend() {
        FakeCommand command = new FakeCommand();
        CommandBusSynchronous commandBusSynchronous = new CommandBusSynchronous(Sets.newHashSet(new FakeSecondCommandMiddleware()), new FakeCommandHandlers());

        CommandResponse<String> response = commandBusSynchronous.send(command);

        assertThat(response.getResponse()).isEqualTo("Success");
    }

    @Test
    public void canSendAndThenInterceptToCommandMiddleware() {
        FakeSecondCommand command = new FakeSecondCommand();
        CommandBusSynchronous commandBusSynchronous = new CommandBusSynchronous(Sets.newHashSet(new FakeSecondCommandMiddleware()), new FakeCommandHandlers());

        CommandResponse<String> response = commandBusSynchronous.send(command);

        assertThat(response.getResponse()).isEqualTo("Success");
        assertThat(middlewareCalled).isTrue();
    }

    @Test
    public void handleUnknownMiddleware() {
        try {
            FakeSecondCommand command = new FakeSecondCommand();
            CommandBusSynchronous commandBusSynchronous = new CommandBusSynchronous(Sets.newHashSet(), new FakeCommandHandlers());
            commandBusSynchronous.send(command);
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("No middleware found for command 'FakeSecondCommand'");
        }
    }

    private static class FakeCommandHandler implements CommandHandler<FakeCommand, String> {
        @Override
        public Pair<String, Event> execute(final FakeCommand fakeCommand) {
            return Pair.of("Success", Nothing.INSTANCE);
        }
    }

    static class FakeCommand implements Command<String> {
    }

    public class FakeCommandHandlers extends CommandHandlers {
        FakeCommandHandlers() {
            super(Sets.newHashSet(new FakeCommandHandler()));
        }
    }

    public class FakeSecondCommandMiddleware implements CommandMiddleware<AnotherCommand> {
        @Override
        public <T> Pair<T, Event> intercept(final CommandBus bus, final AnotherCommand fakeCommand, final Supplier<Pair<T, Event>> supplier) {
            middlewareCalled = true;
            return Pair.of((T) "Success", Nothing.INSTANCE);
        }
    }

    private class FakeSecondCommand implements AnotherCommand<String> {
    }

    private boolean middlewareCalled;
}