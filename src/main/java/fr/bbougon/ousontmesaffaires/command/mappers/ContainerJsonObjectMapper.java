package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.bbougon.ousontmesaffaires.command.container.ContainerField;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.function.Function;

public class ContainerJsonObjectMapper implements JsonMapper<Container, JsonObject, ContainerField> {

    ContainerJsonObjectMapper() {
    }

    @Override
    public JsonObject map(final Container container, ContainerField containerField) {
        JsonArray jsonItems = new JsonArray();
        container.getItems().forEach(item -> {
            JsonObject itemJson = new JsonObject();
            JsonObject featureJson = new JsonObject();
            item.getFeatures().forEach(feature -> featureJson.addProperty(feature.getType(), feature.getFeature()));
            itemJson.add("item", featureJson);
            JsonArray images = buildImages(item);
            if(images.size() > 0) {
                itemJson.add("images", images);
            }
            itemJson.addProperty("hash", SecurityService.sha1().encrypt(item.toString().getBytes()));
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

    private JsonArray buildImages(final Item item) {
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
        return images;
    }

    @Override
    public JsonObject map(final List<Container> objects, final Function<Container, ContainerField> containerField) {
        throw new NotImplementedException();
    }

}
