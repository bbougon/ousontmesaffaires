package fr.bbougon.ousontmesaffaires.repositories.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domain.container.image.ImageStore;
import org.mongolink.domain.mapper.AggregateMap;

@SuppressWarnings("UnusedDeclaration")
public class ImageStoreMapping extends AggregateMap<ImageStore> {
    @Override
    public void map() {
        property().onProperty(ImageStore::getFolder);
        collection().onProperty(ImageStore::getImages);
    }
}
