package fr.bbougon.ousontmesaffaires.command.extracteditem;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import java.util.UUID;

public class ExtractedItemGetAllCommand implements Command<String> {
    public String fromUuid(final UUID uuid) {
        return new Codec().urlSafeToBase64(uuid.toString());
    }
}
