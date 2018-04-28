package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ContainerPatchCommandHandler implements CommandHandler<ContainerPatchCommand, String> {

    @Override
    public Pair<String, Object> execute(final ContainerPatchCommand containerPatchCommand) {
        Container container = Repositories.containerRepository().findById(containerPatchCommand.getUUID());
        container.setDescription(containerPatchCommand.getPatch().getDescription());
        String result = new GsonBuilder().create().toJson(
                container.toJsonObject(containerPatchCommand.getContainerId()));
        return Pair.of(result, container);
    }
}
