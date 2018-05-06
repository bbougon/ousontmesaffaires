package fr.bbougon.ousontmesaffaires.command.mappers;

public class JsonMappers {

    public static ContainerJsonObjectMapper fromContainer() {
        return new ContainerJsonObjectMapper();
    }

    public static ContainerJsonArrayMapper fromContainers() {
        return new ContainerJsonArrayMapper();
    }

    public static SignatureJsonObjectMapper fromSignature() {
        return new SignatureJsonObjectMapper();
    }
}
