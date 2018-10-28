package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.container.FoundContainer;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.apache.commons.lang3.tuple.Pair;

public class ItemDestinationCommandHandler implements CommandHandler<ItemDestinationCommand, FoundContainer> {

    @Override
    public Pair<FoundContainer, NextEvent> execute(final ItemDestinationCommand itemDestinationCommand) {
        Container container = Repositories.containerRepository()
                .get(itemDestinationCommand.getContainerUUID())
                .orElseThrow(() -> new BusinessError("UNEXISTING_CONTAINER"));
        Container existingContainer = Repositories.containerRepository()
                .get(itemDestinationCommand.getDestinationContainerUUID())
                .orElseThrow(() -> new BusinessError("UNEXISTING_DESTINATION_CONTAINER"));
        return container.moveItemTo(itemDestinationCommand, existingContainer)
                .<Pair<FoundContainer, NextEvent>>map(movedItem -> Pair.of(
                        new FoundContainer(new Codec().urlSafeToBase64(existingContainer.getId().toString()), existingContainer.getName(),
                                existingContainer.getDescription(), existingContainer.getItems()), movedItem))
                .orElseGet(() -> Pair.of(null, Nothing.INSTANCE));
    }

}
