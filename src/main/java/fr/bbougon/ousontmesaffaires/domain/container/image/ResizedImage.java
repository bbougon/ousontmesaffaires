package fr.bbougon.ousontmesaffaires.domain.container.image;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ResizedImage {
    public static ResizedImage create(final String url, final String secureUrl, final double height, final double width) {
        ResizedImage resizedImage = new ResizedImage();
        resizedImage.url = url;
        resizedImage.secureUrl = secureUrl;
        resizedImage.width = width;
        resizedImage.height = height;
        return resizedImage;
    }

    public String getUrl() {
        return url;
    }

    public String getSecureUrl() {
        return secureUrl;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ResizedImage that = (ResizedImage) o;

        return new EqualsBuilder()
                .append(url, that.url)
                .append(secureUrl, that.secureUrl)
                .append(width, that.width)
                .append(height, that.height)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(url)
                .append(secureUrl)
                .append(width)
                .append(height)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ResizedImage{" +
                "url='" + url + '\'' +
                ", secureUrl='" + secureUrl + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    private String url;
    private String secureUrl;
    private Double width;
    private Double height;
}
