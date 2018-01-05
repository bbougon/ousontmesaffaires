package fr.bbougon.ousontmesaffaires.web.ressources.json;

import com.google.gson.*;

public class ItemJSON {

    private ItemJSON(final String item) {
        this.item = item;
    }

    private String item;

    public static ItemJSON from(final String payload) {
        JsonElement parse = new JsonParser().parse(payload);
        JsonObject item = ((JsonObject) parse).getAsJsonObject("item");
        return new ItemJSON(item.toString());
    }

    public String getPayload() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(item);
    }

}
