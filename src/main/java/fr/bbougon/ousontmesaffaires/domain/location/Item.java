package fr.bbougon.ousontmesaffaires.domain.location;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

public class Item {

    @SuppressWarnings("UnusedDeclaration")
    Item() {
    }

    private Item(final List<Feature> features) {
        this.features.addAll(features);
    }

    public static Item create(final List<Feature> features) {
        return new Item(features);
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    private Set<Feature> features = Sets.newHashSet();
}
