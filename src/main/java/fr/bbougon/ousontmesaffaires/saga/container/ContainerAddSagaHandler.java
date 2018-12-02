package fr.bbougon.ousontmesaffaires.saga.container;

import fr.bbougon.ousontmesaffaires.command.container.NLPCommand;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.saga.SagaHandler;

import java.util.UUID;
import java.util.stream.Collectors;

public class ContainerAddSagaHandler implements SagaHandler<ContainerAddSaga, UUID> {

    @Override
    public UUID run(final CommandBus bus, final ContainerAddSaga saga) {
        Container container = Container.create(saga.getContainerName(),
                saga.getItems().stream()
                        .map(Item::create)
                        .collect(Collectors.toList()));
        Repositories.containerRepository().persist(container);
        bus.send(new NLPCommand(container.getItems(), container.getId()));
        return container.getId();
    }
}
