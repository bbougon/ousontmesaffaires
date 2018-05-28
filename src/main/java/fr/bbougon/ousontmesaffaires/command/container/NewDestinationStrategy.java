package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class NewDestinationStrategy implements DestinationStrategy{
    @Override
    public Pair<String, Object> apply(final Container container, final Item itemToMove) {
        Container newContainer = container.moveToNewContainer(itemToMove);
        Repositories.containerRepository().persist(newContainer);
        return Pair.of(newContainer.getId().toString(), newContainer);
    }
}
