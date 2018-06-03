package fr.bbougon.ousontmesaffaires.command.mappers;

public class JsonMappers {

    public static ContainerJsonMapper fromContainer() {
        return new ContainerJsonMapper();
    }

    public static SignatureJsonMapper fromSignature() {
        return new SignatureJsonMapper();
    }

    public static ExtractedItemJsonMapper fromExtractedItem() {
        return new ExtractedItemJsonMapper();
    }
}
