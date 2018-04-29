package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ContainersGetCommandHandler implements CommandHandler<ContainersGetCommand, String> {

    @Override
    public Pair<String, Object> execute(final ContainersGetCommand containersGetCommand) {
        List<Container> containers = Repositories.containerRepository().getAll();
        return Pair.of(new GsonBuilder()
                        .create()
                        .toJson(JsonMappers
                                .fromContainers()
                                .map(containers, (Container container) -> new ContainerField(containersGetCommand.fromUUID(container.getId()), containersGetCommand.getQrCode(container.getId())))),
                Nothing.INSTANCE);
    }

}
