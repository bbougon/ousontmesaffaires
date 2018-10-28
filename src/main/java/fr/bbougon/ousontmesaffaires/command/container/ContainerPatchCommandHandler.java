package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.command.mappers.PatchMapper;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.PatchedContainer;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ContainerPatchCommandHandler implements CommandHandler<ContainerPatchCommand, String> {

    @Override
    public Pair<String, NextEvent> execute(final ContainerPatchCommand containerPatchCommand) {
        Container containerToPatch = Repositories.containerRepository()
                .get(containerPatchCommand.getUUID())
                .orElseThrow(() -> new BusinessError("UNEXISTING_CONTAINER"));
        PatchedContainer patchedContainer = new PatchMapper().map(containerPatchCommand.getPatch()).apply(() -> containerToPatch);
        String result = new GsonBuilder()
                .create()
                .toJson(JsonMappers.fromContainer()
                                .map(containerToPatch));
        return Pair.of(result, patchedContainer);
    }

}
