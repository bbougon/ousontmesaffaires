package fr.bbougon.ousontmesaffaires.command;

import fr.bbougon.ousontmesaffaires.command.location.*;

import javax.inject.Inject;

public class CommandHandlers {

    public LocationAddCommandHandler locationAdd() {
        return locationAddCommandHandler;
    }

    public ItemAddToLocationCommandHandler itemAddToLocation() {
        return itemAddToLocationCommandHandler;
    }

    public LocationGetCommandHandler locationGet() {
        return locationGetCommandHandler;
    }

    @Inject
    private LocationAddCommandHandler locationAddCommandHandler;

    @Inject
    private ItemAddToLocationCommandHandler itemAddToLocationCommandHandler;

    @Inject
    private LocationGetCommandHandler locationGetCommandHandler;

}
