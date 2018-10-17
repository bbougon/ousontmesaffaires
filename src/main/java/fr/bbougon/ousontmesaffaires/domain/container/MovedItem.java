package fr.bbougon.ousontmesaffaires.domain.container;

import fr.bbougon.ousontmesaffaires.command.NextEvent;

public class MovedItem extends NextEvent<Item> {
    public MovedItem(final String itemHash) {
        this.itemHash = itemHash;
    }

    public String getItemHash() {
        return itemHash;
    }

    private String itemHash;
}
