package fr.bbougon.ousontmesaffaires.domain.container;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return new EqualsBuilder()
                .append(features, item.features)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(features)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Item{" +
                "features=" + features +
                '}';
    }
}
