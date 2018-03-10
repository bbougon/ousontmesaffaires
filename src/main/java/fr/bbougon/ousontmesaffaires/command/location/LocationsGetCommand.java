package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.ressources.LocationResource;

import javax.ws.rs.core.UriInfo;
import java.util.UUID;

public class LocationsGetCommand implements Command<String> {

    public LocationsGetCommand(final Codec codec, final QRGenerator qrGenerator, final UriInfo uriInfo) {
        this.codec = codec;
        this.qrGenerator = qrGenerator;
        this.uriInfo = uriInfo;
    }

    public String getQrCode(final UUID id) {
        return qrGenerator.encodeToBase64(uriInfo.getAbsolutePath() + "/" + getId(id));
    }

    public String getId(final UUID id) {
        return codec.urlSafeToBase64(id.toString());
    }

    private final Codec codec;
    private final QRGenerator qrGenerator;
    private final UriInfo uriInfo;
}
