package fr.bbougon.ousontmesaffaires.container;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoundContainer {
    public FoundContainer(final String id, final String name, final String description, final List<fr.bbougon.ousontmesaffaires.domain.container.Item> items) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.items = items.stream().map(item -> new Item(item.getItemHash(), item.getItem(), item.getImageStore(), item.getFeatures())).collect(Collectors.toList());
    }

    public final String id;
    public final String name;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public final String description;
    public final List<Item> items;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {

        public Item(final String itemHash, final String item, final fr.bbougon.ousontmesaffaires.domain.container.image.ImageStore imageStore, final Set<fr.bbougon.ousontmesaffaires.domain.container.Feature> features) {
            this.item = item;
            this.imageStore = new ImageStore(imageStore.getFolder(), imageStore.getImages());
            this.itemHash = itemHash;
            this.features = features.stream()
                    .map(feature -> new Feature(feature.getType(), feature.getFeature()))
                    .collect(Collectors.toSet());
        }


        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Image {

            Image(final String url, final String secureUrl, final String signature, final List<fr.bbougon.ousontmesaffaires.domain.container.image.ResizedImage> resizedImages) {
                this.url = url;
                this.secureUrl = secureUrl;
                this.signature = signature;
                this.resizedImages = resizedImages.stream()
                        .map(resizedImage -> new ResizedImage(resizedImage.getUrl(), resizedImage.getSecureUrl(), resizedImage.getHeight(), resizedImage.getWidth()))
                        .collect(Collectors.toList());
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            private class ResizedImage {
                ResizedImage(final String url, final String secureUrl, final Double height, final Double width) {
                    this.url = url;
                    this.secureUrl = secureUrl;
                    this.height = height;
                    this.width = width;
                }

                public final String url;
                public final String secureUrl;
                public final Double height;
                public final Double width;
            }

            public final String url;
            public final String secureUrl;
            public final String signature;
            public final List<ResizedImage> resizedImages;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private class Feature {
            Feature(final String type, final String feature) {
                this.type = type;
                this.feature = feature;
            }

            public final String type;
            public final String feature;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class ImageStore {
            ImageStore(final String folder, final List<fr.bbougon.ousontmesaffaires.domain.container.image.Image> images) {
                this.folder = folder;
                this.images = images.stream()
                        .map(image -> new Image(image.getUrl(), image.getSecureUrl(), image.getSignature(), image.getResizedImages()))
                        .collect(Collectors.toList());
            }

            public final String folder;
            public final List<Image> images;
        }

        public final String item;
        public final ImageStore imageStore;
        public final String itemHash;
        public final Set<Feature> features;
    }
}
