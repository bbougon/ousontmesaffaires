package fr.bbougon.ousontmesaffaires.command.container;

import java.util.HashMap;

public class ContainerField extends HashMap<String, String> {

    public ContainerField(final String containerId, final String qrCode) {
        this.containerId = containerId;
        this.qrCode = qrCode;
    }

    public ContainerField(final String containerId) {
        this.containerId = containerId;
        this.qrCode = null;
    }

    public String getContainerId() {
        return containerId;
    }

    public String getQrCode() {
        return qrCode;
    }

    private final String containerId;
    private final String qrCode;
}
