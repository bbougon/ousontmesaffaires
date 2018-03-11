package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import javax.ws.rs.core.UriInfo;
import java.util.UUID;

public class LocationGetCommand implements Command<String> {

    public LocationGetCommand(final String base64LocationId, final UriInfo uriInfo, final QRGenerator qrGenerator) {
        Codec codec = new Codec();
        this.id = base64LocationId;
        this.uuid = codec.fromString(codec.fromBase64(this.id));
        qrCode = qrGenerator.encodeToBase64(uriInfo.getAbsolutePath().toASCIIString());
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getQrCode() {
        return qrCode;
    }

    public String getId() {
        return id;
    }

    private final UUID uuid;
    private final String qrCode;
    private final String id;
}
