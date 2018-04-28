package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ContainerPatchCommandHandler implements CommandHandler<ContainerPatchCommand, Nothing>{

    @Override
    public Pair<Nothing, Object> execute(final ContainerPatchCommand containerPatchCommand) {
        Container container = Repositories.containerRepository().findById(containerPatchCommand.getUUID());
        container.setDescription(containerPatchCommand.getPatch().getDescription());
        return Pair.of(Nothing.INSTANCE, container);
    }
}
