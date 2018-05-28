package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import java.util.UUID;

public class ItemNewDestinationCommand implements Command<String> {
    public ItemNewDestinationCommand(final String containerId, final String itemHash) {
        this.itemHash = itemHash;
        Codec codec = new Codec();
        containerUUID = codec.fromString(codec.fromBase64(containerId));

    }

    public UUID getContainerUUID() {
        return containerUUID;
    }

    public String getItemHash() {
        return itemHash;
    }

    private UUID containerUUID;
    private String itemHash;
}
