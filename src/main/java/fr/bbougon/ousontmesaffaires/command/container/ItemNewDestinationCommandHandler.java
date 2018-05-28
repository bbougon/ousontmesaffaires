package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public class ItemNewDestinationCommandHandler implements CommandHandler<ItemNewDestinationCommand, String> {

    @Override
    public Pair<String, Object> execute(final ItemNewDestinationCommand itemNewDestinationCommand) {
        Container container = Repositories.containerRepository().findById(itemNewDestinationCommand.getContainerUUID());
        Optional<Item> itemToMove = container.getItems()
                .stream()
                .filter(item -> SecurityService.sha1().encrypt(new ItemStringFormatter(item).format().getBytes()).equals(itemNewDestinationCommand.getItemHash()))
                .findFirst();
        if(itemToMove.isPresent()) {
            Container newContainer = container.moveToNewContainer(itemToMove.get());
            Repositories.containerRepository().persist(newContainer);
            String json = new GsonBuilder().create()
                    .toJson(JsonMappers.fromContainer().map(newContainer, new ContainerField(new Codec().toBase64(newContainer.getId().toString().getBytes()))));
            return Pair.of(json, newContainer);
        }
        return null;
    }

}
