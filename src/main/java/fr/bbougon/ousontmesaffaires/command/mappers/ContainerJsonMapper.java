package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import java.util.List;

public class ContainerJsonMapper implements JsonMapper<Container> {

    ContainerJsonMapper() {
    }

    @Override
    public JsonElement map(final Container container) {
        ImmutableList<Item> items = container.getItems();
        JsonElement jsonItems = JsonMappers.fromItem().map(items);
        JsonObject containerJson = new JsonObject();
        containerJson.addProperty("id", new Codec().urlSafeToBase64(container.getId().toString()));
        containerJson.addProperty("name", container.getName());
        containerJson.add("items", jsonItems);
        containerJson.addProperty("description", container.getDescription());
        return containerJson;
    }

    @Override
    public JsonElement map(final List<Container> containers) {
        JsonArray jsonArray = new JsonArray();
        containers
                .stream()
                .map(this::map)
                .forEach(jsonArray::add);
        return jsonArray;
    }

}
