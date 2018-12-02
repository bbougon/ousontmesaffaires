package fr.bbougon.ousontmesaffaires.domain.container;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.domain.container.image.Image;
import fr.bbougon.ousontmesaffaires.domain.container.image.ImageStore;
import fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Item {

    @SuppressWarnings("UnusedDeclaration")
    Item() {
    }

    private Item(final String item) {
        this.item = item;
    }

    public static Item create(final String name) {
        Item item = new Item(name);
        item.initiateImageStore(new Codec().toBase64(UUID.randomUUID().toString().getBytes()));
        return item;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public List<Image> getImages() {
        return imageStore.getImages();
    }

    public void add(final Image image) {
        imageStore.add(image);
    }

    public ImageStore getImageStore() {
        return imageStore;
    }

    private void initiateImageStore(final String folderName) {
        this.imageStore = new ImageStore(folderName);
    }

    public String getItem() {
        return item;
    }

    public String getItemHash() {
        return SecurityService.sha1().cypher(new ItemStringFormatter(this).format().getBytes());
    }

    public void processAnalysis(final NLPAnalysis nlpAnalysis) {
        features.add(Feature.create("categories", nlpAnalysis.categories.get(0).hierarchy));
        features.add(Feature.create("concepts", nlpAnalysis.concepts.get(0).name));
        features.add(Feature.create("entities", nlpAnalysis.entitiesAnalyses.entities.get(0).type));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return new EqualsBuilder()
                .append(this.item, item.item)
                .append(imageStore, item.imageStore)
                .isEquals();
    }

    private Set<Feature> features = Sets.newHashSet();
    private ImageStore imageStore;
    private String item;
}
