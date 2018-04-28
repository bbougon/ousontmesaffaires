package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.bbougon.ousontmesaffaires.domain.container.Container;

public class ContainerJsonMapper extends AbstractJsonObjectMapper<Container> {

    ContainerJsonMapper(final String containerId, final String qrCode) {
        this.containerId = containerId;
        this.qrCode = qrCode;
    }

    ContainerJsonMapper(final String containerId) {
        this.containerId = containerId;
    }

    @Override
    public JsonObject map(final Container container) {
        JsonObject containerJson = toJsonObject(container, containerId);
        if (qrCode != null) {
            containerJson.addProperty("qrcode", qrCode);
        }
        return containerJson;
    }

    private JsonObject toJsonObject(final Container container, final String containerId) {
        JsonArray jsonItems = new JsonArray();
        container.getItems().forEach(item -> {
            JsonObject itemJson = new JsonObject();
            JsonObject featureJson = new JsonObject();
            item.getFeatures().forEach(feature -> featureJson.addProperty(feature.getType(), feature.getFeature()));
            itemJson.add("item", featureJson);
            jsonItems.add(itemJson);
        });
        JsonObject containerJson = new JsonObject();
        containerJson.addProperty("id", containerId);
        containerJson.addProperty("name", container.getName());
        containerJson.add("items", jsonItems);
        containerJson.addProperty("description", container.getDescription());
        return containerJson;
    }

    private final String containerId;
    private String qrCode;
}
