package fr.bbougon.ousontmesaffaires.domain.container.image;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class Image {

    Image() {
    }

    public String getSignature() {
        return signature;
    }

    public String getUrl() {
        return url;
    }

    public String getSecureUrl() {
        return secureUrl;
    }

    public List<ResizedImage> getResizedImages() {
        return resizedImages;
    }

    public static Image create(final String signature, final String url, final String secureUrl, final List<ResizedImage> resizedImages) {
        Image image = new Image();
        image.signature = signature;
        image.url = url;
        image.secureUrl = secureUrl;
        image.resizedImages.addAll(resizedImages);
        return image;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        return new EqualsBuilder()
                .append(signature, image.signature)
                .append(url, image.url)
                .append(secureUrl, image.secureUrl)
                .append(resizedImages, image.resizedImages)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(signature)
                .append(url)
                .append(secureUrl)
                .append(resizedImages)
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("signature", signature)
                .add("url", url)
                .add("secureUrl", secureUrl)
                .add("resizedImages", resizedImages)
                .toString();
    }

    private String signature;
    private String url;
    private String secureUrl;
    private List<ResizedImage> resizedImages = Lists.newArrayList();
}
