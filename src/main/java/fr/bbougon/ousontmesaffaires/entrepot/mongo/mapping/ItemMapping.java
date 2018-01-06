package fr.bbougon.ousontmesaffaires.entrepot.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Item;
import org.mongolink.domain.mapper.ComponentMap;

@SuppressWarnings("UnusedDeclaration")
public class ItemMapping extends ComponentMap<Item> {

    @Override
    public void map() {
        collection().onProperty(Item::getFeatures);
    }
}
