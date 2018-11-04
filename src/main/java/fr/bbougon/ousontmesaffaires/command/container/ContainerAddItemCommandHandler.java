package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.ContainerItemAdded;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ContainerAddItemCommandHandler implements CommandHandler<ContainerAddItemCommand, String> {

    public Pair<String, Event> execute(final ContainerAddItemCommand containerAddItemCommand) {
        Container container = Repositories.containerRepository().get(containerAddItemCommand.getUuid())
                .orElseThrow(() -> new BusinessError("UNEXISTING_CONTAINER"));
        ContainerItemAdded containerItemAdded = container.addItem(containerAddItemCommand.getItem());
        return Pair.of(containerItemAdded.itemHash, containerItemAdded);
    }

}
