package fr.bbougon.ousontmesaffaires.domain.container;

import fr.bbougon.ousontmesaffaires.command.Event;

public abstract class PatchedContainer<T> extends Event<Container> {

    PatchedContainer() {
    }

    public abstract T getPatchedData();
}
