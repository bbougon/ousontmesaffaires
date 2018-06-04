package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ContainerName;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import org.apache.commons.lang3.tuple.Pair;

import java.util.UUID;

public class ContainerAddCommandHandler implements CommandHandler<ContainerAddCommand, UUID> {

    public Pair<UUID, Object> execute(final ContainerAddCommand containerAddCommand) {
        Container container = Container.create(ContainerName.getNameFromPayload(containerAddCommand.getPayload()),
                Item.create(Features.getFeaturesFromPayload(containerAddCommand.getPayload())));
        Repositories.containerRepository().persist(container);
        return Pair.of(container.getId(), container);
    }
}
