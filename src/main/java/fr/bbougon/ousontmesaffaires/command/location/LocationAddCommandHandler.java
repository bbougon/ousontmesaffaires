package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkTransaction;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

import java.util.UUID;

public class LocationAddCommandHandler implements CommandHandler<LocationAddCommand, UUID> {

    @MongolinkTransaction
    public Pair<UUID, Object> execute(final LocationAddCommand locationAddCommand) {
        Location location = new Location();
        location.add(locationAddCommand.getItem());
        Repositories.locationRepository().persist(location);
        return Pair.of(location.getId(), location);
    }
}
