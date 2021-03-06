package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.container.FoundContainer;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import java.util.UUID;

public class ContainerPatchCommand implements Command<FoundContainer> {

    public ContainerPatchCommand(final String containerId, final String patch) {
        this.patch = patch;
        Codec codec = new Codec();
        uuid = codec.fromString(codec.fromBase64(containerId));
    }

    public String getPatch() {
        return patch;
    }

    public UUID getUUID() {
        return uuid;
    }

    private final String patch;
    private final UUID uuid;
}
