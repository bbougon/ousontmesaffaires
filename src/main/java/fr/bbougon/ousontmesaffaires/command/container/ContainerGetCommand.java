package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import javax.ws.rs.core.UriInfo;
import java.util.UUID;

public class ContainerGetCommand implements Command<String> {

    public ContainerGetCommand(final String base64ContainerId) {
        Codec codec = new Codec();
        this.id = base64ContainerId;
        this.uuid = codec.fromString(codec.fromBase64(this.id));
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getId() {
        return id;
    }

    private final UUID uuid;
    private final String id;
}
