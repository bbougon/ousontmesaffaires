package fr.bbougon.ousontmesaffaires.repositories.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domain.location.Type;
import org.mongolink.domain.mapper.ComponentMap;

@SuppressWarnings("UnusedDeclaration")
public class TypeMapping extends ComponentMap<Type> {

    @Override
    public void map() {
        property().onProperty(Type::getType);
    }
}
