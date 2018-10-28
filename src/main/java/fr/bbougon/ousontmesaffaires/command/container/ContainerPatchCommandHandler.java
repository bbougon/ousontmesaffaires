package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.command.mappers.PatchMapper;
import fr.bbougon.ousontmesaffaires.container.FoundContainer;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.PatchedContainer;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.apache.commons.lang3.tuple.Pair;

public class ContainerPatchCommandHandler implements CommandHandler<ContainerPatchCommand, FoundContainer> {

    @Override
    public Pair<FoundContainer, NextEvent> execute(final ContainerPatchCommand containerPatchCommand) {
        Container containerToPatch = Repositories.containerRepository()
                .get(containerPatchCommand.getUUID())
                .orElseThrow(() -> new BusinessError("UNEXISTING_CONTAINER"));
        PatchedContainer patchedContainer = new PatchMapper().map(containerPatchCommand.getPatch()).apply(() -> containerToPatch);
        FoundContainer foundContainer = new FoundContainer(new Codec().urlSafeToBase64(containerToPatch.getId().toString()),
                containerToPatch.getName(), containerToPatch.getDescription(), containerToPatch.getItems());
        return Pair.of(foundContainer, patchedContainer);
    }

}
