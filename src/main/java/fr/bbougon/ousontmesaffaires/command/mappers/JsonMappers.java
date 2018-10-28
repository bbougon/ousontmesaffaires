package fr.bbougon.ousontmesaffaires.command.mappers;

public class JsonMappers {

    public static ContainerJsonMapper fromContainer() {
        return new ContainerJsonMapper();
    }

    public static ExtractedItemJsonMapper fromExtractedItem() {
        return new ExtractedItemJsonMapper();
    }

    public static ItemJsonMapper fromItem() {
        return new ItemJsonMapper();
    }
}
