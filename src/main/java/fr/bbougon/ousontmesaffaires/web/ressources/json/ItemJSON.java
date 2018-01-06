package fr.bbougon.ousontmesaffaires.web.ressources.json;

import com.google.gson.*;

public class ItemJSON {

    private ItemJSON(final JsonObject item) {
        this.item = item;
    }

    private JsonObject item;

    public static ItemJSON from(final String payload) {
        JsonElement parse = new JsonParser().parse(payload);
        JsonObject item = ((JsonObject) parse).getAsJsonObject("item");
        return new ItemJSON(item);
    }

    public String getPayload() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(item);
    }

}
