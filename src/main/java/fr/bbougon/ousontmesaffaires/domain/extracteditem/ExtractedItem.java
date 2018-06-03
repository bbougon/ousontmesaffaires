package fr.bbougon.ousontmesaffaires.domain.extracteditem;

import fr.bbougon.ousontmesaffaires.domain.AggregateRoot;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;

import java.util.UUID;

public class ExtractedItem extends AggregateRoot {

    ExtractedItem() {
        super(UUID.randomUUID());
    }

    public static ExtractedItem create(final Item item, final Container container) {
        ExtractedItem extractedItem = new ExtractedItem();
        extractedItem.item = item;
        extractedItem.sourceContainer = container;
        return extractedItem;
    }

    public Item getItem() {
        return item;
    }

    public Container getSourceContainer() {
        return sourceContainer;
    }

    private Item item;
    private Container sourceContainer;
}
