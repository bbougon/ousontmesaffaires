package fr.bbougon.ousontmesaffaires.command;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.command.container.ContainerAddCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ContainerAddItemCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ContainerGetCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ContainerPatchCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ContainersGetCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ItemDestinationCommandHandler;
import fr.bbougon.ousontmesaffaires.command.extracteditem.ExtractedItemAddItemCommandHandler;
import fr.bbougon.ousontmesaffaires.command.extracteditem.ExtractedItemGetAllCommandHandler;
import fr.bbougon.ousontmesaffaires.command.extracteditem.ExtractedItemGetCommandHandler;
import fr.bbougon.ousontmesaffaires.command.sign.SignCommandHandler;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.module.transactional.CommandBusSynchronous;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.rules.ExternalResource;

import java.util.function.Function;
import java.util.function.Supplier;

public class WithCommandBus extends ExternalResource {

    public void apply(final Function<CommandBus, CommandBus> function) {
        CommandBus commandBus = new CommandBusSynchronous(new CommandMiddlewares(Sets.newHashSet(new CommandMiddleware<FakeCommand>() {
            @Override
            public <T> Pair<T, Event> intercept(final CommandBus bus, final FakeCommand command, final Supplier<Pair<T, Event>> supplier) {
                return null;
            }
        })), new CommandHandlersForTest(Sets.newHashSet(
                new ContainerAddCommandHandler(),
                new ContainerAddItemCommandHandler(),
                new ContainerGetCommandHandler(),
                new ContainersGetCommandHandler(),
                new ContainerPatchCommandHandler(),
                new SignCommandHandler(),
                new ItemDestinationCommandHandler(),
                new ExtractedItemAddItemCommandHandler(),
                new ExtractedItemGetAllCommandHandler(),
                new ExtractedItemGetCommandHandler()
        )));
        function.apply(commandBus);
    }

    private class FakeCommand implements Command {
    }
}
