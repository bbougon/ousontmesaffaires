package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public class ItemDestinationCommandHandler implements CommandHandler<ItemDestinationCommand, String> {

    @Override
    public Pair<String, Object> execute(final ItemDestinationCommand itemDestinationCommand) {
        Container container = Repositories.containerRepository().findById(itemDestinationCommand.getContainerUUID());
        if (container == null) {
            return Pair.of(null, null);
        }
        Optional<Item> itemToMove = container.getItems()
                .stream()
                .filter(item -> SecurityService.sha1().encrypt(new ItemStringFormatter(item).format().getBytes()).equals(itemDestinationCommand.getItemHash()))
                .findFirst();
        if (itemToMove.isPresent()) {
            Container existingContainer = Repositories.containerRepository().findById(itemDestinationCommand.getDestinationContainerUUID());
            container.moveToExistingContainer(existingContainer, itemToMove.get());
            return Pair.of(new GsonBuilder().create()
                            .toJson(JsonMappers.fromContainer()
                                    .map(existingContainer)),
                    existingContainer);
        }
        return Pair.of(null, null);
    }

}
