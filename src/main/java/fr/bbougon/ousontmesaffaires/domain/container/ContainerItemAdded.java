package fr.bbougon.ousontmesaffaires.domain.container;

import fr.bbougon.ousontmesaffaires.command.Event;

public class ContainerItemAdded extends Event<Item> {
    public ContainerItemAdded(final String itemHash) {
        this.itemHash = itemHash;
    }

    public String itemHash;
}
