package fr.bbougon.ousontmesaffaires.command;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.saga.container.ContainerAddSagaHandler;
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
import fr.bbougon.ousontmesaffaires.saga.SagaMiddleware;
import fr.bbougon.ousontmesaffaires.saga.item.ContainerAddItemSagaHandler;
import org.junit.rules.ExternalResource;

import java.util.function.Function;

public class WithCommandBus extends ExternalResource {

    public void apply(final Function<CommandBus, CommandBus> function) {
        CommandBus commandBus = new CommandBusSynchronous(Sets.newHashSet(new SagaMiddleware(Sets.newHashSet(
                new ContainerAddItemSagaHandler(),
                new ContainerAddSagaHandler()
        ))),
                new CommandHandlersForTest(Sets.newHashSet(
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

}
