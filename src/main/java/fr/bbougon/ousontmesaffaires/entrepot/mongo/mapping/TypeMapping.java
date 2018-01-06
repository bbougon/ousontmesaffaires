package fr.bbougon.ousontmesaffaires.entrepot.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Type;
import org.mongolink.domain.mapper.ComponentMap;

public class TypeMapping extends ComponentMap<Type> {

    @Override
    public void map() {
        property().onProperty(Type::getType);
    }
}
