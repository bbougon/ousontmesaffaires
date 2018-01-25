package fr.bbougon.ousontmesaffaires.command;

import fr.bbougon.ousontmesaffaires.command.location.*;

import javax.inject.Inject;

public class CommandHandlers {

    @Inject
    private LocationAddCommandHandler locationAddCommandHandler;

    @Inject
    private ItemAddToLocationCommandHandler itemAddToLocationCommandHandler;

    public LocationGetCommandHandler locationGet() {
        return locationGetCommandHandler;
    }

    public LocationAddCommandHandler locationAdd() {
        return locationAddCommandHandler;
    }

    public ItemAddToLocationCommandHandler itemAddToLocation() {
        return itemAddToLocationCommandHandler;
    }
    @Inject
    private LocationGetCommandHandler locationGetCommandHandler;

}
