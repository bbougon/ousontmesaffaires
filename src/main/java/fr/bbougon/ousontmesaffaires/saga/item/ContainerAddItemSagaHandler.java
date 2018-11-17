package fr.bbougon.ousontmesaffaires.saga.item;

import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.ContainerItemAdded;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.saga.SagaHandler;

public class ContainerAddItemSagaHandler implements SagaHandler<ContainerAddItemSaga, String> {

    @Override
    public String run(final CommandBus bus, final ContainerAddItemSaga saga) {
        Container container = Repositories.containerRepository().get(saga.getUuid())
                .orElseThrow(() -> new BusinessError("UNEXISTING_CONTAINER"));
        ContainerItemAdded containerItemAdded = container.addItem(saga.getItem());
        return containerItemAdded.itemHash;
    }
}
