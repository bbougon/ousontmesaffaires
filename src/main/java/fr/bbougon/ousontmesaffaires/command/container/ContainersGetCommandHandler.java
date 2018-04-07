package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class ContainersGetCommandHandler implements CommandHandler<ContainersGetCommand, String> {

    @Override
    public Pair<String, Object> execute(final ContainersGetCommand containersGetCommand) {
        List<Container> containers = Repositories.containerRepository().getAll();
        return Pair.of(new GsonBuilder().create().toJson(getJsonElements(containersGetCommand, containers)), Nothing.INSTANCE);
    }

    private JsonArray getJsonElements(final ContainersGetCommand containersGetCommand, final List<Container> containers) {
        JsonArray jsonArray = new JsonArray();
        containers.stream()
                .map(container -> container.toJsonObject(
                        containersGetCommand.getId(container.getId()), containersGetCommand.getQrCode(container.getId())))
                .collect(Collectors.toList())
                .forEach(jsonArray::add);
        return jsonArray;
    }

}
