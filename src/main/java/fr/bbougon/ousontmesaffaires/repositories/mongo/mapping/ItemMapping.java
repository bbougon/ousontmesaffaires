package fr.bbougon.ousontmesaffaires.repositories.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domain.container.Item;
import org.mongolink.domain.mapper.ComponentMap;

@SuppressWarnings("UnusedDeclaration")
public class ItemMapping extends ComponentMap<Item> {

    @Override
    public void map() {
        collection().onProperty(Item::getFeatures);
        collection().onProperty(Item::getImages);
    }
}
