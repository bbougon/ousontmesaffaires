package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.ContainerItemAdded;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ContainerAddItemCommandHandler implements CommandHandler<ContainerAddItemCommand, Nothing> {

    public Pair<Nothing, NextEvent> execute(final ContainerAddItemCommand containerAddItemCommand) {
        Container container = Repositories.containerRepository().get(containerAddItemCommand.getUuid())
                .orElseThrow(() -> new BusinessError("UNEXISTING_CONTAINER"));
        container.add(containerAddItemCommand.getItem());
        return Pair.of(Nothing.INSTANCE, new ContainerItemAdded(container.getItems().get(container.getItems().size() - 1).getItemHash()));
    }

}
