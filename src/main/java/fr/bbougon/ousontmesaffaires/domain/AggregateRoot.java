package fr.bbougon.ousontmesaffaires.domain;

import java.util.UUID;

public abstract class AggregateRoot {

    public AggregateRoot(final UUID uuid) {
        id = uuid;
    }

    public UUID getId() {
        return id;
    }

    protected final UUID id;
}
