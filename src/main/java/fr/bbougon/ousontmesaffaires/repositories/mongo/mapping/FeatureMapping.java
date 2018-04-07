package fr.bbougon.ousontmesaffaires.repositories.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domain.container.Feature;
import org.mongolink.domain.mapper.ComponentMap;

@SuppressWarnings("UnusedDeclaration")
public class FeatureMapping extends ComponentMap<Feature> {

    @Override
    public void map() {
        property().onProperty(Feature::getType);
        property().onProperty(Feature::getFeature);
    }
}
