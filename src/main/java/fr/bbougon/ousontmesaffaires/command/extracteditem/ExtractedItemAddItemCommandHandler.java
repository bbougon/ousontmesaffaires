package fr.bbougon.ousontmesaffaires.command.extracteditem;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;
import java.util.UUID;

public class ExtractedItemAddItemCommandHandler implements CommandHandler<ExtractedItemAddItemCommand, UUID> {

    @Override
    public Pair<UUID, Object> execute(final ExtractedItemAddItemCommand extractedItemAddItemCommand) {
        Container container = Repositories.containerRepository().findById(extractedItemAddItemCommand.getContainerId());
        Optional<Item> optionalItem = container.getItems().stream()
                .filter(item -> SecurityService.sha1().encrypt(new ItemStringFormatter(item).format().getBytes()).equals(extractedItemAddItemCommand.getItemHash()))
                .findFirst();
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            ExtractedItem extractedItem = ExtractedItem.create(item, container);
            Repositories.extractedItemRepository().persist(extractedItem);
            container.removeItem(item);
            return Pair.of(extractedItem.getId(), extractedItem);
        }
        return Pair.of(null, null);
    }
}
