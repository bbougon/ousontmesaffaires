package fr.bbougon.ousontmesaffaires.command.extracteditem;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import java.util.UUID;

public class ExtractedItemAddItemCommand implements Command<UUID> {

    public ExtractedItemAddItemCommand(final String containerId, final String itemHash) {
        Codec codec = new Codec();
        this.containerId = codec.fromString(codec.fromBase64(containerId));
        this.itemHash = itemHash;
    }

    public UUID getContainerId() {
        return containerId;
    }

    public String getItemHash() {
        return itemHash;
    }

    private UUID containerId;
    private String itemHash;
}
