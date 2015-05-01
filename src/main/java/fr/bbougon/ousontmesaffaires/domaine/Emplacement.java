package fr.bbougon.ousontmesaffaires.domaine;

import java.util.UUID;

public class Emplacement {

    public Emplacement() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    private final UUID id;
}
