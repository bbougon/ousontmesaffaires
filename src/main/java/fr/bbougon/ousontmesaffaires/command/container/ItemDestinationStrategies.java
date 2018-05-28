package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import org.apache.commons.lang3.tuple.Pair;

public class ItemDestinationStrategies {

    private ItemDestinationStrategies(final ItemDestinationCommand itemDestinationCommand) {
        strategy = itemDestinationCommand.targetExistingDestination() ? new ExistingDestinationStrategy(itemDestinationCommand) : new NewDestinationStrategy();
    }

    static Pair<String, Object> apply(final ItemDestinationCommand itemDestinationCommand, final Container container, final Item itemToMove) {
        return new ItemDestinationStrategies(itemDestinationCommand).apply(container, itemToMove);
    }

    private Pair<String, Object> apply(final Container container, final Item itemToMove) {
        return strategy.apply(container, itemToMove);
    }

    private final DestinationStrategy strategy;
}
