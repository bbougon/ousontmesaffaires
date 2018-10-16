package fr.bbougon.ousontmesaffaires.command.container;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import fr.bbougon.ousontmesaffaires.command.Command;

import java.util.List;
import java.util.UUID;

public class ContainerAddCommand implements Command<UUID> {

    public ContainerAddCommand(final String payload) {
        this.payload = payload;
        JsonObject container = new JsonParser().parse(payload).getAsJsonObject();
        containerName = container.get("name").getAsString();
        JsonArray jsonArray = container.get("items").getAsJsonArray();
        jsonArray.forEach(jsonElement -> items.add(jsonElement.getAsJsonObject().get("item").getAsString()));
    }

    public String getPayload() {
        return payload;
    }

    public String getContainerName() {
        return containerName;
    }

    public List<String> getItems() {
        return items;
    }

    private final String containerName;
    private final List<String> items = Lists.newArrayList();

    private String payload;
}
