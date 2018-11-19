package fr.bbougon.ousontmesaffaires.container;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoundContainer {

    public FoundContainer() {
    }

    public FoundContainer(final String id, final String name, final String description, final List<fr.bbougon.ousontmesaffaires.domain.container.Item> items) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.items = items.stream().map(item -> new Item(item.getItemHash(), item.getItem(), item.getImageStore(), item.getFeatures())).collect(Collectors.toList());
    }

    public String id;
    public String name;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public String description;
    public List<Item> items;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {

        public Item() {
        }

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

            public Image() {
            }

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

                public ResizedImage() {
                }

                ResizedImage(final String url, final String secureUrl, final Double height, final Double width) {
                    this.url = url;
                    this.secureUrl = secureUrl;
                    this.height = height;
                    this.width = width;
                }

                public String url;
                public String secureUrl;
                public Double height;
                public Double width;
            }

            public String url;
            public String secureUrl;
            public String signature;
            public List<ResizedImage> resizedImages;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private class Feature {

            public Feature() {
            }

            Feature(final String type, final String feature) {
                this.type = type;
                this.feature = feature;
            }

            public String type;
            public String feature;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class ImageStore {

            public ImageStore() {
            }

            ImageStore(final String folder, final List<fr.bbougon.ousontmesaffaires.domain.container.image.Image> images) {
                this.folder = folder;
                this.images = images.stream()
                        .map(image -> new Image(image.getUrl(), image.getSecureUrl(), image.getSignature(), image.getResizedImages()))
                        .collect(Collectors.toList());
            }

            public String folder;
            public List<Image> images;
        }

        public String item;
        public ImageStore imageStore;
        public String itemHash;
        public Set<Feature> features;
    }
}
