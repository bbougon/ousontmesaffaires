package fr.bbougon.ousontmesaffaires.infrastructure.module.transactional;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.CommandHandlers;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandBusSynchronousTest {

    @Test
    public void canSend() {
        CommandBusSynchronous commandBusSynchronous = new CommandBusSynchronous(new FakeCommandHandlers());

        CommandResponse<String> response = commandBusSynchronous.send(new FakeCommand());

        assertThat(response.getResponse()).isEqualTo("Success");
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
        public FakeCommandHandlers() {
            super(Sets.newHashSet(new FakeCommandHandler()));
        }
    }
}