package fr.bbougon.ousontmesaffaires.repositories.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import org.mongolink.domain.mapper.AggregateMap;

@SuppressWarnings("UnusedDeclaration")
public class ExtractedItemMapping extends AggregateMap<ExtractedItem> {

    @Override
    public void map() {
        id().onProperty(ExtractedItem::getId).natural();
        property().onProperty(ExtractedItem::getItem);
        property().onProperty(ExtractedItem::getSourceContainer);
    }
}
