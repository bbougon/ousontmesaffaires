package fr.bbougon.ousontmesaffaires.web.ressources.json;

import com.google.gson.*;

public class ContainerName {

    public static String getNameFromPayload(final String payload) {
        return ((JsonObject) new JsonParser().parse(payload)).get("name").getAsString();
    }
}
