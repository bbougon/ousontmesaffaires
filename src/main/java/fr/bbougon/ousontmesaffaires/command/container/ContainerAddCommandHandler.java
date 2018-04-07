package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

import java.util.UUID;

public class ContainerAddCommandHandler implements CommandHandler<ContainerAddCommand, UUID> {

    public Pair<UUID, Object> execute(final ContainerAddCommand containerAddCommand) {
        Container container = containerAddCommand.getContainer();
        Repositories.containerRepository().persist(container);
        return Pair.of(container.getId(), container);
    }
}
