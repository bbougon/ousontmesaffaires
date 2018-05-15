package fr.bbougon.ousontmesaffaires.web.helpers;

import fr.bbougon.ousontmesaffaires.domain.container.Item;

import java.util.stream.Collectors;

public class ItemStringFormatter {

    public ItemStringFormatter(final Item item) {
        this.item = item;
    }

    public String format() {
        String features = item.getFeatures().stream()
                .map(feature -> String.format("%s;%s", feature.getType(), feature.getFeature()))
                .collect(Collectors.joining(";"));
        String folder = item.getImageStore().getFolder();
        String images = item.getImages().stream()
                .map(image -> {
                    String resizedImages = image.getResizedImages().stream()
                            .map(resizedImage -> String.format("%s;%s;%s;%s", resizedImage.getUrl(), resizedImage.getSecureUrl(), resizedImage.getHeight(), resizedImage.getWidth()))
                            .collect(Collectors.joining(";"));
                    return String.format("%s;%s;%s;%s", image.getSignature(), image.getUrl(), image.getSecureUrl(), resizedImages);
                })
                .collect(Collectors.joining("|"));

        return String.format("%s|%s|%s", features, folder, images);
    }

    private final Item item;
}
