package fr.bbougon.ousontmesaffaires.domain.container.image;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class ImageStore {

    @SuppressWarnings("UnusedDeclaration")
    ImageStore() {
    }

    public ImageStore(final String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }

    public List<Image> getImages() {
        return images;
    }

    public void add(final Image image) {
        images.add(image);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ImageStore that = (ImageStore) o;

        return new EqualsBuilder()
                .append(folder, that.folder)
                .append(images, that.images)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(folder)
                .append(images)
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("folder", folder)
                .add("images", images)
                .toString();
    }

    private String folder;
    private List<Image> images = Lists.newArrayList();
}
