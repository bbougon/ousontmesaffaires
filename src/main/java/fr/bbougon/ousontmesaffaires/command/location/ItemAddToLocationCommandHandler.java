package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkTransaction;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ItemAddToLocationCommandHandler implements CommandHandler<ItemAddToLocationCommand, Nothing> {

    @MongolinkTransaction
    public Pair<Nothing, Object> execute(final ItemAddToLocationCommand itemAddToLocationCommand) {
        Location location = Repositories.locationRepository().findById(itemAddToLocationCommand.getUuid());
        if(location == null) {
            return Pair.of(Nothing.INSTANCE, null);
        }
        location.add(itemAddToLocationCommand.getItem());
        return Pair.of(Nothing.INSTANCE, location);
    }

}
