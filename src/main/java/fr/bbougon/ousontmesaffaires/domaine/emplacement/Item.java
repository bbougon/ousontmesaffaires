package fr.bbougon.ousontmesaffaires.domaine.emplacement;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ItemJSON;

import java.util.Set;

public class Item {

    @SuppressWarnings("UnusedDeclaration")
    public Item() {
    }

    private Item(final String payload) {
        JsonElement jsonElement = new JsonParser().parse(payload);
        Set<String> keys = jsonElement.getAsJsonObject().keySet();
        keys.forEach(key -> this.features.add(new Feature(new Type(key), jsonElement.getAsJsonObject().get(key).getAsString())));
    }

    public static Item create(final ItemJSON article) {
        return new Item(article.getPayload());
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    private Set<Feature> features = Sets.newHashSet();
}
