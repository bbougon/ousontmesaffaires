package fr.bbougon.ousontmesaffaires.entrepot.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Feature;
import org.mongolink.domain.mapper.ComponentMap;

@SuppressWarnings("UnusedDeclaration")
public class FeatureMapping extends ComponentMap<Feature> {

    @Override
    public void map() {
        property().onProperty(Feature::getType);
        property().onProperty(Feature::getFeature);
    }
}
