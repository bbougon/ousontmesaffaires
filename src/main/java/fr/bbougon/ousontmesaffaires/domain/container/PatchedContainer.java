package fr.bbougon.ousontmesaffaires.domain.container;

import fr.bbougon.ousontmesaffaires.command.NextEvent;

public abstract class PatchedContainer<T> extends NextEvent<Container> {

    PatchedContainer() {
    }

    public abstract T getPatchedData();
}
