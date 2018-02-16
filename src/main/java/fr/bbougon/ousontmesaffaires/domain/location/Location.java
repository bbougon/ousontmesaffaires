package fr.bbougon.ousontmesaffaires.domain.location;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.*;

import java.util.List;
import java.util.UUID;

public class Location {

    Location() {
        id = UUID.randomUUID();
    }

    public Location(final String location, final Item item) {
        this();
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public static Location create(final String locationName, final Item item) {
        return new Location(locationName, item);
    }

    public JsonObject toJsonObject(final String locationId, final String qrCode) {
        JsonArray items = new JsonArray();
        getItems().forEach(item -> {
            JsonObject itemJson = new JsonObject();
            JsonObject featureJson = new JsonObject();
            item.getFeatures().forEach(feature -> featureJson.addProperty(feature.getType(), feature.getFeature()));
            itemJson.add("item", featureJson);
            items.add(itemJson);
        });
        JsonObject locationJson = new JsonObject();
        locationJson.addProperty("id", locationId);
        locationJson.addProperty("location", getLocation());
        locationJson.add("items", items);
        locationJson.addProperty("qrcode", qrCode);
        return locationJson;
    }

    private final UUID id;
    private String location;
    private List<Item> items = Lists.newArrayList();
}
