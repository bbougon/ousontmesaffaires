package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.domain.location.Item;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;

import java.util.UUID;

public class LocationAddCommand implements Command<UUID> {

    public LocationAddCommand(final String payload) {
        this.item = Item.create(Features.getFeaturesFromPayload(payload));
    }

    public Item getItem() {
        return item;
    }

    private final Item item;
}
