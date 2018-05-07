package fr.bbougon.ousontmesaffaires.domain.container;

import com.google.common.collect.Lists;

import java.util.List;

public class Image {

    private Image() {
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

    private String signature;
    private String url;
    private String secureUrl;
    private List<ResizedImage> resizedImages = Lists.newArrayList();
}
