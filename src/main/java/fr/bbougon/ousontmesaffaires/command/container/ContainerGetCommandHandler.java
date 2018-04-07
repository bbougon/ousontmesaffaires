package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ContainerGetCommandHandler implements CommandHandler<ContainerGetCommand, String> {

    @Override
    public Pair<String, Object> execute(final ContainerGetCommand containerGetCommand) {
        Container container = Repositories.containerRepository().findById(containerGetCommand.getUUID());
        String result = new GsonBuilder().create().toJson(
                container.toJsonObject(containerGetCommand.getId(), containerGetCommand.getQrCode()));
        return Pair.of(result, container);
    }

}
