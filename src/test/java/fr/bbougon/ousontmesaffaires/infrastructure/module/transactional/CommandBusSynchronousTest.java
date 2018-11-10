package fr.bbougon.ousontmesaffaires.infrastructure.module.transactional;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.CommandHandlers;
import fr.bbougon.ousontmesaffaires.command.CommandMiddleware;
import fr.bbougon.ousontmesaffaires.command.CommandMiddlewares;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandBusSynchronousTest {

    @Test
    public void canSend() {
        FakeCommand command = new FakeCommand();
        CommandBusSynchronous commandBusSynchronous = new CommandBusSynchronous(new CommandMiddlewares(Sets.newHashSet(new CommandMiddleware<FakeCommand>() {
            @Override
            public <T> Pair<T, Event> intercept(final CommandBus bus, final FakeCommand command, final Supplier<Pair<T, Event>> supplier) {
                return null;
            }
        })), new FakeCommandHandlers());

        CommandResponse<String> response = commandBusSynchronous.send(command);

        assertThat(response.getResponse()).isEqualTo("Success");
    }

    @Test
    public void canSendAndThenInterceptToCommandMiddleware() {
        FakeCommand command = new FakeCommand();
        CommandBusSynchronous commandBusSynchronous = new CommandBusSynchronous(new CommandMiddlewares(Sets.newHashSet(new CommandMiddleware<FakeCommand>() {
            @Override
            public <T> Pair<T, Event> intercept(final CommandBus bus, final FakeCommand command, final Supplier<Pair<T, Event>> supplier) {
                middlewareCalled = true;
                return null;
            }
        })), new FakeCommandHandlers());

        CommandResponse<String> response = commandBusSynchronous.send(command);

        assertThat(response.getResponse()).isEqualTo("Success");
        assertThat(middlewareCalled).isTrue();
    }

    private static class FakeCommandHandler implements CommandHandler<FakeCommand, String> {
        @Override
        public Pair<String, Event> execute(final FakeCommand fakeCommand) {
            return Pair.of("Success", Nothing.INSTANCE);
        }
    }

    private static class FakeCommand implements Command<String> {
    }

    private class FakeCommandHandlers extends CommandHandlers {
        FakeCommandHandlers() {
            super(Sets.newHashSet(new FakeCommandHandler()));
        }
    }

    private boolean middlewareCalled;
}