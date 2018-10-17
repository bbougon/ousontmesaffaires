package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.PatchedContainer;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ContainerPatchCommandHandler implements CommandHandler<ContainerPatchCommand, String> {

    @Override
    public Pair<String, NextEvent> execute(final ContainerPatchCommand containerPatchCommand) {
        Container containerToMap = Repositories.containerRepository().get(containerPatchCommand.getUUID()).get();
        containerPatchCommand.getPatch().apply(() -> containerToMap);
        String result = new GsonBuilder()
                .create()
                .toJson(JsonMappers.fromContainer()
                                .map(containerToMap));
        return Pair.of(result, new PatchedContainer(containerToMap.getDescription(), containerToMap.getItems()));
    }
}
