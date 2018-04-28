package fr.bbougon.ousontmesaffaires.command.mappers;

public class JsonMappers {

    public static ContainerJsonMapper fromContainer(final String containerId, final String qrCode) {
        return new ContainerJsonMapper(containerId, qrCode);
    }

    public static ContainerJsonMapper fromContainer(final String containerId) {
        return new ContainerJsonMapper(containerId);
    }
}
