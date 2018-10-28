package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.container.FoundContainer;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class ContainersGetCommandHandler implements CommandHandler<ContainersGetCommand, List<FoundContainer>> {

    @Override
    public Pair<List<FoundContainer>, NextEvent> execute(final ContainersGetCommand containersGetCommand) {
        List<Container> containers = Repositories.containerRepository().getAll();
        List<FoundContainer> foundContainers = containers.stream()
                .map(container -> new FoundContainer(new Codec().urlSafeToBase64(container.getId().toString()),
                        container.getName(), container.getDescription(), container.getItems()))
                .collect(Collectors.toList());
        return Pair.of(foundContainers, Nothing.INSTANCE);
    }

}
