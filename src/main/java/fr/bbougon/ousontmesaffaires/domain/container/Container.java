package fr.bbougon.ousontmesaffaires.domain.container;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.*;

import java.util.List;
import java.util.UUID;

public class Container {

    Container() {
        id = UUID.randomUUID();
    }

    public Container(final String name, final Item item) {
        this();
        this.name = name;
        this.items.add(item);
    }

    public UUID getId() {
        return id;
    }

    public ImmutableList<Item> getItems() {
        return ImmutableList.<Item>builder().addAll(items).build();
    }

    public void add(final Item item) {
        items.add(item);
    }

    public String getName() {
        return name;
    }

    public static Container create(final String containerName, final Item item) {
        return new Container(containerName, item);
    }

    public JsonObject toJsonObject(final String containerId, final String qrCode) {
        JsonArray items = new JsonArray();
        getItems().forEach(item -> {
            JsonObject itemJson = new JsonObject();
            JsonObject featureJson = new JsonObject();
            item.getFeatures().forEach(feature -> featureJson.addProperty(feature.getType(), feature.getFeature()));
            itemJson.add("item", featureJson);
            items.add(itemJson);
        });
        JsonObject containerJson = new JsonObject();
        containerJson.addProperty("id", containerId);
        containerJson.addProperty("name", getName());
        containerJson.add("items", items);
        containerJson.addProperty("qrcode", qrCode);
        return containerJson;
    }

    private final UUID id;
    private String name;
    private List<Item> items = Lists.newArrayList();
}
