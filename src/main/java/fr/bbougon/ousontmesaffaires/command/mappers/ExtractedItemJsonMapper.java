package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.bbougon.ousontmesaffaires.command.extracteditem.ExtractedItemField;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;

import java.util.List;
import java.util.function.Function;

public class ExtractedItemJsonMapper implements JsonMapper<ExtractedItem, ExtractedItemField> {

    @Override
    public JsonElement map(final List<ExtractedItem> extractedItems, final Function<ExtractedItem, ExtractedItemField> function) {
        JsonArray extractedItemsArray = new JsonArray();
        extractedItems.forEach(extractedItem -> {
            JsonObject extractedItemObject = new JsonObject();
            extractedItemObject.addProperty("id", new Codec().urlSafeToBase64(extractedItem.getId().toString()));
            JsonObject itemJson = new JsonObject();
            JsonObject featureJson = new JsonObject();
            extractedItem.getItem().getFeatures().forEach(feature -> featureJson.addProperty(feature.getType(), feature.getFeature()));
            itemJson.add("item", featureJson);
            itemJson.add("imageStore", buildImages(extractedItem.getItem()));
            itemJson.addProperty("hash", SecurityService.sha1().encrypt(new ItemStringFormatter(extractedItem.getItem()).format().getBytes()));
            extractedItemObject.add("item", itemJson);
            extractedItemsArray.add(extractedItemObject);
        });
        return extractedItemsArray;
    }

    private JsonElement buildImages(final Item item) {
        JsonObject imageStore = new JsonObject();
        imageStore.addProperty("folder", item.getImageStore().getFolder());
        JsonArray images = new JsonArray();
        item.getImages().forEach(image -> {
            JsonObject imageJson = new JsonObject();
            imageJson.addProperty("signature", image.getSignature());
            imageJson.addProperty("url", image.getUrl());
            imageJson.addProperty("secureUrl", image.getSecureUrl());
            JsonArray resizedImages = new JsonArray();
            image.getResizedImages().forEach(resizedImage -> {
                JsonObject resizedImageJson = new JsonObject();
                resizedImageJson.addProperty("url", resizedImage.getUrl());
                resizedImageJson.addProperty("secureUrl", resizedImage.getSecureUrl());
                resizedImageJson.addProperty("height", resizedImage.getHeight());
                resizedImageJson.addProperty("width", resizedImage.getWidth());
                resizedImages.add(resizedImageJson);
            });
            imageJson.add("resizedImages", resizedImages);
            images.add(imageJson);
        });
        if(images.size() > 0) {
            imageStore.add("images", images);
        }
        return imageStore;
    }

    @Override
    public JsonElement map(final ExtractedItem object, final ExtractedItemField function) {
        return null;
    }
}
