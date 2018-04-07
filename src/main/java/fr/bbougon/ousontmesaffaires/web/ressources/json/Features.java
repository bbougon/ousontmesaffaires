package fr.bbougon.ousontmesaffaires.web.ressources.json;

import com.google.gson.*;
import fr.bbougon.ousontmesaffaires.domain.container.Feature;

import java.util.List;
import java.util.stream.Collectors;

public class Features {

    public static List<Feature> getFeaturesFromPayload(final String payload) {
        return ItemJSON.from(payload).getFeatures().stream()
                .map(feature -> Feature.create(feature.getType(), feature.getValue()))
                .collect(Collectors.toList());
    }

    static class ItemJSON {

        private ItemJSON(final List<FeatureJSON> features) {
            this.features = features;
        }

        static ItemJSON from(final String payload) {
            JsonObject item = ((JsonObject) new JsonParser().parse(payload)).getAsJsonObject("item");
            JsonElement jsonElement = item.getAsJsonObject();
            List<FeatureJSON> features = jsonElement.getAsJsonObject().keySet().stream()
                    .map(key -> new FeatureJSON(key, jsonElement.getAsJsonObject().get(key).getAsString()))
                    .collect(Collectors.toList());
            return new ItemJSON(features);
        }

        List<FeatureJSON> getFeatures() {
            return features;
        }

        private List<FeatureJSON> features;
    }
}
