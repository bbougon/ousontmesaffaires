package fr.bbougon.ousontmesaffaires.entrepot.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Location;
import org.mongolink.domain.mapper.AggregateMap;

@SuppressWarnings("UnusedDeclaration")
public class LocationMapping extends AggregateMap<Location> {

    @Override
    public void map() {
        id().onProperty(Location::getId).natural();
        collection().onProperty(Location::getItems);
    }
}
