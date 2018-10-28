package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ItemDestinationCommandHandler implements CommandHandler<ItemDestinationCommand, String> {

    @Override
    public Pair<String, NextEvent> execute(final ItemDestinationCommand itemDestinationCommand) {
        Container container = Repositories.containerRepository().get(itemDestinationCommand.getContainerUUID()).orElse(null);
        Container existingContainer = Repositories.containerRepository().get(itemDestinationCommand.getDestinationContainerUUID()).get();
        if (container == null) {
            return Pair.of(null, null);
        }
        return container.moveItemTo(itemDestinationCommand, existingContainer)
                .<Pair<String, NextEvent>>map(movedItem -> Pair.of(new GsonBuilder().create().toJson(JsonMappers.fromContainer().map(existingContainer)), movedItem))
                .orElseGet(() -> Pair.of(null, Nothing.INSTANCE));
    }

}
