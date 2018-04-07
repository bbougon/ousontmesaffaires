package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ItemAddToContainerCommandHandler implements CommandHandler<ItemAddToContainerCommand, Nothing> {

    public Pair<Nothing, Object> execute(final ItemAddToContainerCommand itemAddToContainerCommand) {
        Container container = Repositories.containerRepository().findById(itemAddToContainerCommand.getUuid());
        if(container == null) {
            return Pair.of(Nothing.INSTANCE, null);
        }
        container.add(itemAddToContainerCommand.getItem());
        return Pair.of(Nothing.INSTANCE, container);
    }

}
