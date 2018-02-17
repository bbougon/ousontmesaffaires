package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

import java.util.UUID;

public class LocationAddCommandHandler implements CommandHandler<LocationAddCommand, UUID> {

    public Pair<UUID, Object> execute(final LocationAddCommand locationAddCommand) {
        Location location = locationAddCommand.getLocation();
        Repositories.locationRepository().persist(location);
        return Pair.of(location.getId(), location);
    }
}
