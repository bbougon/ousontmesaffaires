package fr.bbougon.ousontmesaffaires.entrepot.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Emplacement;
import org.mongolink.domain.mapper.AggregateMap;

@SuppressWarnings("UnusedDeclaration")
public class EmplacementMapping extends AggregateMap<Emplacement> {

    @Override
    public void map() {
        id().onProperty(Emplacement::getId).natural();
        collection().onProperty(Emplacement::getArticles);
    }
}
