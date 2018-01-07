package fr.bbougon.ousontmesaffaires.command;

import fr.bbougon.ousontmesaffaires.command.location.ItemAddToLocationCommandHandler;
import fr.bbougon.ousontmesaffaires.command.location.LocationAddCommandHandler;
import fr.bbougon.ousontmesaffaires.repositories.LocationRepository;

import javax.inject.Inject;

public class CommandHandlers {

    @SuppressWarnings("UnusedDeclaration")
    public CommandHandlers() {
    }

    public CommandHandlers(final LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public LocationAddCommandHandler locationAdd() {
        return new LocationAddCommandHandler(locationRepository);
    }

    public ItemAddToLocationCommandHandler itemAddToLocation() {
        return new ItemAddToLocationCommandHandler(locationRepository);
    }

    @Inject
    LocationRepository locationRepository;

}
