package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import java.util.UUID;

public class ItemDestinationCommand implements Command<String> {
    public ItemDestinationCommand(final String containerId, final String itemHash, final Destination destination) {
        this.itemHash = itemHash;
        Codec codec = new Codec();
        containerUUID = codec.fromString(codec.fromBase64(containerId));
        destinationContainerUUID = codec.fromString(codec.fromBase64(destination.getDestination()));
    }

    public UUID getContainerUUID() {
        return containerUUID;
    }

    public String getItemHash() {
        return itemHash;
    }

    public UUID getDestinationContainerUUID() {
        return destinationContainerUUID;
    }

    private UUID containerUUID;
    private UUID destinationContainerUUID;
    private String itemHash;
}
