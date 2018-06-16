package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;

import java.util.List;

public class ItemJsonMapper implements JsonMapper<Item> {

    private static JsonElement buildImages(final Item item) {
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
        if (images.size() > 0) {
            imageStore.add("images", images);
        }
        return imageStore;
    }

    @Override
    public JsonElement map(final List<Item> items) {
        JsonArray jsonItems = new JsonArray();
        items.forEach(item -> jsonItems.add(JsonMappers.fromItem().map(item)));
        return jsonItems;
    }

    @Override
    public JsonElement map(final Item item) {
        JsonObject itemJson = new JsonObject();
        JsonObject featureJson = new JsonObject();
        item.getFeatures().forEach(feature -> featureJson.addProperty(feature.getType(), feature.getFeature()));
        itemJson.add("item", featureJson);
        itemJson.add("imageStore", buildImages(item));
        itemJson.addProperty("hash", SecurityService.sha1().cypher(new ItemStringFormatter(item).format().getBytes()));
        return itemJson;
    }
}
