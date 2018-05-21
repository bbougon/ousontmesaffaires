package fr.bbougon.ousontmesaffaires.domain.container.image;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;

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

    private String folder;
    private List<Image> images = Lists.newArrayList();
}
