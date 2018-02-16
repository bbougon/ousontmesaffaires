package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import java.util.UUID;

public class LocationsGetCommand implements Command<String> {

    public LocationsGetCommand(final Codec codec, final QRGenerator qrGenerator) {
        this.codec = codec;
        this.qrGenerator = qrGenerator;
    }

    public String getQrCode(final UUID id) {
        return qrGenerator.encodeToBase64(id.toString());
    }

    public String getId(final UUID id) {
        return codec.urlSafeToBase64(id.toString());
    }

    private final Codec codec;
    private final QRGenerator qrGenerator;
}
