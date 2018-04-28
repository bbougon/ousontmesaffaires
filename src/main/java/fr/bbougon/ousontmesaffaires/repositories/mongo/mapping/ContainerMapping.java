package fr.bbougon.ousontmesaffaires.repositories.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domain.container.Container;
import org.mongolink.domain.mapper.AggregateMap;

@SuppressWarnings("UnusedDeclaration")
public class ContainerMapping extends AggregateMap<Container> {

    @Override
    public void map() {
        id().onProperty(Container::getId).natural();
        property().onProperty(Container::getName);
        property().onProperty(Container::getDescription);
        collection().onProperty(Container::getItems);
    }
}
