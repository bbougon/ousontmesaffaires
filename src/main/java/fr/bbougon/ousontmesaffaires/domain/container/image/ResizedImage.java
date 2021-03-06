package fr.bbougon.ousontmesaffaires.domain.container.image;

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

    private String url;
    private String secureUrl;
    private Double width;
    private Double height;
}
