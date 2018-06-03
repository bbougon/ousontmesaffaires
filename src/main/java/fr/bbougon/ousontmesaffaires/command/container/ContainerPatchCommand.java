package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.command.Patch;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import java.util.UUID;

public class ContainerPatchCommand implements Command<String> {

    public ContainerPatchCommand(final String containerId, final Patch patch) {
        this.patch = patch;
        Codec codec = new Codec();
        uuid = codec.fromString(codec.fromBase64(containerId));
    }

    public Patch getPatch() {
        return patch;
    }

    public UUID getUUID() {
        return uuid;
    }

    private final Patch patch;
    private final UUID uuid;
}
