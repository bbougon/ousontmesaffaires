package fr.bbougon.ousontmesaffaires.command;

import fr.bbougon.ousontmesaffaires.command.location.ItemAddToLocationCommandHandler;
import fr.bbougon.ousontmesaffaires.command.location.LocationAddCommandHandler;

import javax.inject.Inject;

public class CommandHandlers {

    @Inject
    private LocationAddCommandHandler locationAddCommandHandler;

    @Inject
    private ItemAddToLocationCommandHandler itemAddToLocationCommandHandler;

    public LocationAddCommandHandler locationAdd() {
        return locationAddCommandHandler;
    }

    public ItemAddToLocationCommandHandler itemAddToLocation() {
        return itemAddToLocationCommandHandler;
    }

}
