package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.container.MovedItem;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public class ItemDestinationCommandHandler implements CommandHandler<ItemDestinationCommand, String> {

    @Override
    public Pair<String, NextEvent> execute(final ItemDestinationCommand itemDestinationCommand) {
        Container container = Repositories.containerRepository().get(itemDestinationCommand.getContainerUUID()).orElse(null);
        if (container == null) {
            return Pair.of(null, null);
        }
        Optional<Item> itemToMove = container.getItems()
                .stream()
                .filter(item -> item.getItemHash().equals(itemDestinationCommand.getItemHash()))
                .findFirst();
        if (itemToMove.isPresent()) {
            Container existingContainer = Repositories.containerRepository().get(itemDestinationCommand.getDestinationContainerUUID()).get();
            MovedItem movedItem = container.moveToExistingContainer(existingContainer, itemToMove.get());
            return Pair.of(new GsonBuilder().create()
                            .toJson(JsonMappers.fromContainer()
                                    .map(existingContainer)),
                    movedItem);
        }
        return Pair.of(null, Nothing.INSTANCE);
    }

}
