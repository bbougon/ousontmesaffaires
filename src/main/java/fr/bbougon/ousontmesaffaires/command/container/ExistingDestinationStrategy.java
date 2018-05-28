package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.apache.commons.lang3.tuple.Pair;

class ExistingDestinationStrategy implements DestinationStrategy {

    ExistingDestinationStrategy(final ItemDestinationCommand itemDestinationCommand) {
        this.itemDestinationCommand = itemDestinationCommand;
    }

    @Override
    public Pair<String, Object> apply(final Container container, final Item itemToMove) {
        Container existingContainer = Repositories.containerRepository().findById(itemDestinationCommand.getDestinationContainerUUID());
        container.moveToExistingContainer(existingContainer, itemToMove);
        return Pair.of(new GsonBuilder().create()
                .toJson(JsonMappers
                        .fromContainer()
                        .map(existingContainer, new ContainerField(new Codec().urlSafeToBase64(container.getId().toString())))), existingContainer);
    }

    private final ItemDestinationCommand itemDestinationCommand;
}
