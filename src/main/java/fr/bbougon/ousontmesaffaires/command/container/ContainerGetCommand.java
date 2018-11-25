package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.container.FoundContainer;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import java.util.UUID;

public class ContainerGetCommand implements Command<FoundContainer> {

    public ContainerGetCommand(final String base64ContainerId) {
        Codec codec = new Codec();
        this.uuid = codec.fromString(codec.fromBase64(base64ContainerId));
    }

    public UUID getUUID() {
        return uuid;
    }

    private final UUID uuid;
}
