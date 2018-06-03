package fr.bbougon.ousontmesaffaires.command.extracteditem;

import com.google.gson.Gson;
import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ExtractedItemJSON;

import java.util.UUID;

public class ExtractedItemAddItemCommand implements Command<UUID> {

    public ExtractedItemAddItemCommand(final String payload) {
        ExtractedItemJSON extractedItemJSON = new Gson().fromJson(payload, ExtractedItemJSON.class);
        Codec codec = new Codec();
        this.containerId = codec.fromString(codec.fromBase64(extractedItemJSON.containerId));
        this.itemHash = extractedItemJSON.itemHash;
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
