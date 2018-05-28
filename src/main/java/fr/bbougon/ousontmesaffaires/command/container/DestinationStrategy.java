package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import org.apache.commons.lang3.tuple.Pair;

public interface DestinationStrategy {
    Pair<String,Object> apply(Container container, Item itemToMove);
}
