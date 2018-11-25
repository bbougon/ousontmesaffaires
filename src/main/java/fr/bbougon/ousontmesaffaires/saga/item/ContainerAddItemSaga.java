package fr.bbougon.ousontmesaffaires.saga.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.saga.Saga;

import java.util.UUID;

public class ContainerAddItemSaga implements Saga<String> {

    public ContainerAddItemSaga(final UUID uuid, final String payload) {
        this.uuid = uuid;
        JsonObject container = new JsonParser().parse(payload).getAsJsonObject();
        this.item = container.get("item").getAsString();
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
