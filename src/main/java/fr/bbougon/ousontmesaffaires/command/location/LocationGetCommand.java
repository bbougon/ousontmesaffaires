package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;

import javax.ws.rs.core.UriInfo;
import java.util.UUID;

public class LocationGetCommand implements Command<String> {

    public LocationGetCommand(final UUID uuid, final UriInfo uriInfo, final QRGenerator qrGenerator) {
        this.uuid = uuid;
        qrCode = qrGenerator.encodeToBase64(uriInfo.getAbsolutePath().toASCIIString());
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getQrCode() {
        return qrCode;
    }

    private final UUID uuid;
    private final String qrCode;
}
