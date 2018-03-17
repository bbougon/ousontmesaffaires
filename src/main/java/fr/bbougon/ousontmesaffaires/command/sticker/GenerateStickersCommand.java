package fr.bbougon.ousontmesaffaires.command.sticker;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import java.util.UUID;

public class GenerateStickersCommand implements Command<Sticker> {
    public GenerateStickersCommand(final String base64LocationId, final String forLocation) {
        Codec codec = new Codec();
        uuid = codec.fromString(codec.fromBase64(base64LocationId));
        buildLocationUrl(base64LocationId, forLocation);
    }

    private void buildLocationUrl(final String base64LocationId, final String forLocation) {
        locationUrl = forLocation.endsWith("/") ? forLocation + base64LocationId : forLocation + "/" + base64LocationId;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getLocationUrl() {
        return locationUrl;
    }

    private UUID uuid;
    private String locationUrl;
}
