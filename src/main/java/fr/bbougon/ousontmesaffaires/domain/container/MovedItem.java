package fr.bbougon.ousontmesaffaires.domain.container;

import fr.bbougon.ousontmesaffaires.command.Event;

public class MovedItem extends Event<Item> {
    public MovedItem(final String itemHash) {
        this.itemHash = itemHash;
    }

    public String getItemHash() {
        return itemHash;
    }

    private String itemHash;
}
