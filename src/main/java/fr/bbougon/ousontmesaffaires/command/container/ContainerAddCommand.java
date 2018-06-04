package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.Command;

import java.util.UUID;

public class ContainerAddCommand implements Command<UUID> {

    public ContainerAddCommand(final String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    private String payload;
}
