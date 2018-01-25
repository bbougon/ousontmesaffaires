package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.Command;

import javax.ws.rs.core.UriInfo;
import java.util.UUID;

public class LocationGetCommand implements Command<String> {

    public LocationGetCommand(final UUID uuid, final UriInfo uriInfo) {
        this.uuid = uuid;
        this.uriInfo = uriInfo;
    }

    public UUID getUUID() {
        return uuid;
    }

    public UriInfo getUriInfo() {
        return uriInfo;
    }

    private final UUID uuid;
    private final UriInfo uriInfo;
}
