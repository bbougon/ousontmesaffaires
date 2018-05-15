package fr.bbougon.ousontmesaffaires.domain.container;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.domain.container.image.Image;
import fr.bbougon.ousontmesaffaires.domain.container.image.ImageStore;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Item {

    @SuppressWarnings("UnusedDeclaration")
    Item() {
    }

    private Item(final List<Feature> features) {
        this.features.addAll(features);
    }

    public static Item create(final List<Feature> features) {
        Item item = new Item(features);
        item.initiateImageStore(new Codec().toBase64(UUID.randomUUID().toString().getBytes()));
        return item;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public List<Image> getImages() {
        return imageStore.getImages();
    }

    public void add(final Image image) {
        imageStore.add(image);
    }

    public ImageStore getImageStore() {
        return imageStore;
    }

    private void initiateImageStore(final String folderName) {
        this.imageStore = new ImageStore(folderName);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return new EqualsBuilder()
                .append(features, item.features)
                .append(imageStore, item.imageStore)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(features)
                .append(imageStore)
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("features", features)
                .add("imageStore", imageStore)
                .toString();
    }

    private Set<Feature> features = Sets.newHashSet();

    private ImageStore imageStore;
}
