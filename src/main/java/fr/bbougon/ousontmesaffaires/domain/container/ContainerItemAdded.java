package fr.bbougon.ousontmesaffaires.domain.container;

import fr.bbougon.ousontmesaffaires.command.NextEvent;

public class ContainerItemAdded extends NextEvent<Item> {
    public ContainerItemAdded(final String itemHash) {
        this.itemHash = itemHash;
    }

    private String itemHash;
}
