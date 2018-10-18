package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.command.Nothing;

import java.util.UUID;

public class ContainerAddItemCommand implements Command<Nothing> {

    public ContainerAddItemCommand(final UUID uuid, final String payload) {
        this.uuid = uuid;
        JsonObject container = new JsonParser().parse(payload).getAsJsonObject();
        JsonArray jsonArray = container.get("items").getAsJsonArray();
        this.item = jsonArray.get(0).getAsJsonObject().get("item").getAsString();
    }

    UUID getUuid() {
        return uuid;
    }

    public String getItem() {
        return item;
    }

    private final UUID uuid;
    private final String item;
}
