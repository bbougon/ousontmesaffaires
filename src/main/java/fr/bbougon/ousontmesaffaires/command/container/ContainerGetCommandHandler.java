package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ContainerGetCommandHandler implements CommandHandler<ContainerGetCommand, String> {

    @Override
    public Pair<String, NextEvent> execute(final ContainerGetCommand containerGetCommand) {
        Container containerToMap = Repositories.containerRepository().findById(containerGetCommand.getUUID());
        String result = new GsonBuilder().create().toJson(
                JsonMappers
                        .fromContainer()
                        .map(containerToMap));
        return Pair.of(result, Nothing.INSTANCE);
    }

}
