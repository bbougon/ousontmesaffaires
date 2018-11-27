package fr.bbougon.ousontmesaffaires.saga.container;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.bbougon.ousontmesaffaires.saga.Saga;

import java.util.List;
import java.util.UUID;

public class ContainerAddSaga implements Saga<UUID> {

    public ContainerAddSaga(final String payload) {
        JsonObject container = new JsonParser().parse(payload).getAsJsonObject();
        containerName = container.get("name").getAsString();
        JsonArray jsonArray = container.get("items").getAsJsonArray();
        jsonArray.forEach(jsonElement -> items.add(jsonElement.getAsJsonObject().get("item").getAsString()));
    }

    public String getContainerName() {
        return containerName;
    }

    public List<String> getItems() {
        return items;
    }

    private final String containerName;
    private final List<String> items = Lists.newArrayList();

}
