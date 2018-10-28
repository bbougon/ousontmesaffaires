package fr.bbougon.ousontmesaffaires.container;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.bbougon.ousontmesaffaires.domain.container.Item;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoundExtractedItem {
    public FoundExtractedItem(final String id, final Item item, final String sourceContainerId, final String sourceContainerName) {
        this.id = id;
        this.item = new FoundContainer.Item(item.getItemHash(), item.getItem(), item.getImageStore(), item.getFeatures());
        this.sourceContainerId = sourceContainerId;
        this.sourceContainerName = sourceContainerName;
    }

    public final String id;
    public final FoundContainer.Item item;
    public final String sourceContainerId;
    public final String sourceContainerName;
}
