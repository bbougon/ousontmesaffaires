package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.domain.location.Item;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import fr.bbougon.ousontmesaffaires.web.ressources.json.LocationName;

import java.util.UUID;

public class LocationAddCommand implements Command<UUID> {

    public LocationAddCommand(final String payload) {
        this.location = Location.create(LocationName.getNameFromPayload(payload), Item.create(Features.getFeaturesFromPayload(payload)));
    }

    public Location getLocation() {
        return location;
    }

    private final Location location;
}
