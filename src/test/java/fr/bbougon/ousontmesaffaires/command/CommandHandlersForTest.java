package fr.bbougon.ousontmesaffaires.command;

import fr.bbougon.ousontmesaffaires.command.location.ItemAddToLocationCommandHandler;
import fr.bbougon.ousontmesaffaires.command.location.LocationAddCommandHandler;

public class CommandHandlersForTest extends CommandHandlers {

    @Override
    public LocationAddCommandHandler locationAdd() {
        return new LocationAddCommandHandler();
    }

    @Override
    public ItemAddToLocationCommandHandler itemAddToLocation() {
        return new ItemAddToLocationCommandHandler();
    }
}
