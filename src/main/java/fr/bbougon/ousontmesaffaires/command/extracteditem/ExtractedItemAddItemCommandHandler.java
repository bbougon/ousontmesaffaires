package fr.bbougon.ousontmesaffaires.command.extracteditem;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.UUID;

public class ExtractedItemAddItemCommandHandler implements CommandHandler<ExtractedItemAddItemCommand, UUID> {

    @Override
    public Pair<UUID, Event> execute(final ExtractedItemAddItemCommand extractedItemAddItemCommand) {
        Container container = Repositories.containerRepository().get(extractedItemAddItemCommand.getContainerId()).orElseThrow(() -> new BusinessError("UNEXISTING_CONTAINER"));
        Item foundItem = container.getItems().stream()
                .filter(item -> SecurityService.sha1().cypher(new ItemStringFormatter(item).format().getBytes()).equals(extractedItemAddItemCommand.getItemHash()))
                .findFirst()
                .orElseThrow(() -> new BusinessError("UNKNOWN_ITEM", container.getName()));
        ExtractedItem extractedItem = ExtractedItem.create(foundItem, container);
        Repositories.extractedItemRepository().persist(extractedItem);
        container.removeItem(foundItem);
        return Pair.of(extractedItem.getId(), Nothing.INSTANCE);
    }
}
