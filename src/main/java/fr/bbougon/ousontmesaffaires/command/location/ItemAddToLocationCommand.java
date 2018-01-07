package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.location.Item;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;

import java.util.UUID;

public class ItemAddToLocationCommand implements Command<Nothing> {

    public ItemAddToLocationCommand(final UUID uuid, final String payload) {
        this.uuid = uuid;
        this.item = Item.create(Features.getFeaturesFromPayload(payload));
    }

    UUID getUuid() {
        return uuid;
    }

    public Item getItem() {
        return item;
    }

    private final UUID uuid;
    private final Item item;
}
