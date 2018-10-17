package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

import java.util.UUID;
import java.util.stream.Collectors;

public class ContainerAddCommandHandler implements CommandHandler<ContainerAddCommand, UUID> {

    public Pair<UUID, NextEvent> execute(final ContainerAddCommand containerAddCommand) {
        Container container = Container.create(containerAddCommand.getContainerName(),
                containerAddCommand.getItems().stream()
                        .map(Item::create)
                        .collect(Collectors.toList()));
        Repositories.containerRepository().persist(container);
        return Pair.of(container.getId(), Nothing.INSTANCE);
    }
}
