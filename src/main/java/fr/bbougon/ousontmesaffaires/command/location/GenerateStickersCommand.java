package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.util.UUID;

public class GenerateStickersCommand implements Command<File> {
    public GenerateStickersCommand(final Codec codec, final UriInfo uriInfo, final String base64LocationId) {
        uuid = codec.fromString(codec.fromBase64(base64LocationId));
        locationUrl = uriInfo.getBaseUri().toASCIIString() + "locations/" + base64LocationId;
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
