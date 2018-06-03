package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import java.util.List;

public class ExtractedItemJsonMapper implements JsonMapper<ExtractedItem> {

    @Override
    public JsonElement map(final List<ExtractedItem> extractedItems) {
        JsonArray extractedItemsArray = new JsonArray();
        extractedItems.forEach(extractedItem -> {
            JsonObject extractedItemObject = new JsonObject();
            extractedItemObject.addProperty("id", new Codec().urlSafeToBase64(extractedItem.getId().toString()));
            extractedItemObject.add("item", JsonMappers.fromItem().map(extractedItem.getItem()));
            extractedItemsArray.add(extractedItemObject);
        });
        return extractedItemsArray;
    }

    @Override
    public JsonElement map(final ExtractedItem object) {
        return null;
    }
}
