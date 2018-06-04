package fr.bbougon.ousontmesaffaires.command.extracteditem;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import java.util.UUID;

public class ExtractedItemGetCommand implements Command<String> {
    public ExtractedItemGetCommand(final String extractedItemId) {
        Codec codec = new Codec();
        this.extractedItemUUID = codec.fromString(codec.fromBase64(extractedItemId));
    }

    public UUID getExtractedItemUUID() {
        return extractedItemUUID;
    }

    private final UUID extractedItemUUID;
}
