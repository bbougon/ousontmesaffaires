package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.repositories.LocationRepository;
import fr.bbougon.ousontmesaffaires.repositories.MongoConfiguration;
import org.apache.commons.lang3.tuple.Pair;

import java.util.UUID;

public class LocationAddCommandHandler implements CommandHandler<LocationAddCommand, UUID> {

    private LocationRepository locationRepository;

    public LocationAddCommandHandler(final LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Pair<UUID, Object> execute(final LocationAddCommand locationAddCommand) {
        MongoConfiguration.startSession();
        Location location = new Location();
        location.add(locationAddCommand.getItem());
        locationRepository.persist(location);
        MongoConfiguration.stopSession();
        return Pair.of(location.getId(), location);
    }
}
