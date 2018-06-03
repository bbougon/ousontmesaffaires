package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ContainerAddItemCommandHandler implements CommandHandler<ContainerAddItemCommand, Nothing> {

    public Pair<Nothing, Object> execute(final ContainerAddItemCommand containerAddItemCommand) {
        Container container = Repositories.containerRepository().findById(containerAddItemCommand.getUuid());
        if(container == null) {
            return Pair.of(Nothing.INSTANCE, null);
        }
        container.add(containerAddItemCommand.getItem());
        return Pair.of(Nothing.INSTANCE, container);
    }

}
