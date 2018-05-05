package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ContainerPatchCommandHandler implements CommandHandler<ContainerPatchCommand, String> {

    @Override
    public Pair<String, Object> execute(final ContainerPatchCommand containerPatchCommand) {
        Container containerToMap = Repositories.containerRepository().findById(containerPatchCommand.getUUID());
        containerPatchCommand.getPatch().apply(() -> containerToMap);
        String result = new GsonBuilder()
                .create()
                .toJson(JsonMappers.fromContainer()
                                .map(containerToMap, (Container container) -> new ContainerField(containerPatchCommand.getContainerId())));
        return Pair.of(result, containerToMap);
    }
}
