package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import javax.ws.rs.core.UriInfo;
import java.util.UUID;

public class ContainersGetCommand implements Command<String> {

    public ContainersGetCommand(final QRGenerator qrGenerator, final UriInfo uriInfo) {
        this.qrGenerator = qrGenerator;
        this.uriInfo = uriInfo;
    }

    public String getQrCode(final UUID id) {
        return qrGenerator.encodeToBase64(uriInfo.getAbsolutePath() + "/" + fromUUID(id));
    }

    public String fromUUID(final UUID id) {
        return codec.urlSafeToBase64(id.toString());
    }

    private final Codec codec = new Codec();
    private final QRGenerator qrGenerator;
    private final UriInfo uriInfo;
}
