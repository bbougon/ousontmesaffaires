package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.container.FoundContainer;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.apache.commons.lang3.tuple.Pair;

public class ContainerGetCommandHandler implements CommandHandler<ContainerGetCommand, FoundContainer> {

    @Override
    public Pair<FoundContainer, Event> execute(final ContainerGetCommand containerGetCommand) {
        Container containerToMap = Repositories.containerRepository()
                .get(containerGetCommand.getUUID())
                .orElseThrow(() -> new BusinessError("UNEXISTING_CONTAINER"));
        FoundContainer foundContainer = new FoundContainer(new Codec().urlSafeToBase64(containerToMap.getId().toString()), containerToMap.getName(),
                containerToMap.getDescription(), containerToMap.getItems());
        return Pair.of(foundContainer, Nothing.INSTANCE);
    }

}
