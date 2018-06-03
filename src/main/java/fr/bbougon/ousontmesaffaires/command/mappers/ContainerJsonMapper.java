package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.bbougon.ousontmesaffaires.command.container.ContainerField;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;

import java.util.List;
import java.util.function.Function;

public class ContainerJsonMapper implements JsonMapper<Container, ContainerField> {

    ContainerJsonMapper() {
    }

    @Override
    public JsonElement map(final Container container, ContainerField containerField) {
        JsonArray jsonItems = new JsonArray();
        container.getItems().forEach(item -> {
            JsonObject itemJson = new JsonObject();
            JsonObject featureJson = new JsonObject();
            item.getFeatures().forEach(feature -> featureJson.addProperty(feature.getType(), feature.getFeature()));
            itemJson.add("item", featureJson);
            itemJson.add("imageStore", buildImages(item));
            itemJson.addProperty("hash", SecurityService.sha1().encrypt(new ItemStringFormatter(item).format().getBytes()));
            jsonItems.add(itemJson);
        });
        JsonObject containerJson = new JsonObject();
        containerJson.addProperty("id", containerField.getContainerId());
        containerJson.addProperty("name", container.getName());
        containerJson.add("items", jsonItems);
        containerJson.addProperty("description", container.getDescription());
        if (containerField.getQrCode() != null) {
            containerJson.addProperty("qrcode", containerField.getQrCode());
        }
        return containerJson;
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
    public JsonElement map(final List<Container> containers, final Function<Container, ContainerField> containerField) {
        JsonArray jsonArray = new JsonArray();
        containers
                .stream()
                .map(container -> map(container, containerField.apply(container)))
                .forEach(jsonArray::add);
        return jsonArray;
    }

}
