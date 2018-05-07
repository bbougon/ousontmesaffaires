package fr.bbougon.ousontmesaffaires.domain.container;

import java.math.BigDecimal;

public class ResizedImage {
    public static ResizedImage create(final String url, final String secureUrl, final BigDecimal width, final BigDecimal height) {
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

    public BigDecimal getWidth() {
        return width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    private String url;
    private String secureUrl;
    private BigDecimal width;
    private BigDecimal height;
}
