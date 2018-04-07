package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ContainerName;

import java.util.UUID;

public class ContainerAddCommand implements Command<UUID> {

    public ContainerAddCommand(final String payload) {
        this.container = Container.create(ContainerName.getNameFromPayload(payload), Item.create(Features.getFeaturesFromPayload(payload)));
    }

    public Container getContainer() {
        return container;
    }

    private final Container container;
}
