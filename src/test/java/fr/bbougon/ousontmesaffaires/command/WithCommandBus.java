package fr.bbougon.ousontmesaffaires.command;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.command.container.ContainerAddCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ContainerGetCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ContainerPatchCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ContainersGetCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ItemAddToContainerCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ItemDestinationCommandHandler;
import fr.bbougon.ousontmesaffaires.command.sign.SignCommandHandler;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.module.transactional.TransactionalMiddleware;
import org.junit.rules.ExternalResource;

import java.util.function.Function;

public class WithCommandBus extends ExternalResource {

    public void apply(final Function<CommandBus, CommandBus> function) {
        CommandBus commandBus = new TransactionalMiddleware(new CommandHandlersForTest(Sets.newHashSet(
                new ContainerAddCommandHandler(),
                new ItemAddToContainerCommandHandler(),
                new ContainerGetCommandHandler(),
                new ContainersGetCommandHandler(),
                new ContainerPatchCommandHandler(),
                new SignCommandHandler(),
                new ItemDestinationCommandHandler()
        )));
        function.apply(commandBus);
    }
}
