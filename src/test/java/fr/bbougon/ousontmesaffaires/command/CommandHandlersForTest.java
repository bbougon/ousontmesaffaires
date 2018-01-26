package fr.bbougon.ousontmesaffaires.command;

import fr.bbougon.ousontmesaffaires.command.location.*;

public class CommandHandlersForTest extends CommandHandlers {

    @Override
    public LocationAddCommandHandler locationAdd() {
        return new LocationAddCommandHandler();
    }

    @Override
    public ItemAddToLocationCommandHandler itemAddToLocation() {
        return new ItemAddToLocationCommandHandler();
    }

    @Override
    public LocationGetCommandHandler locationGet() {
        return new LocationGetCommandHandler();
    }

}
