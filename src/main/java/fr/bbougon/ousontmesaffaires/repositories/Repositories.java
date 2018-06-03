package fr.bbougon.ousontmesaffaires.repositories;

import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItemRepository;
import fr.bbougon.ousontmesaffaires.domain.container.ContainerRepository;

import javax.inject.Inject;

public abstract class Repositories {

    public static void initialise(Repositories repositories) {
        Repositories.repositories = repositories;
    }

    public static ContainerRepository containerRepository() {
        return repositories.getContainerRepository();
    }

    protected abstract ContainerRepository getContainerRepository();

    public static ExtractedItemRepository extractedItemRepository() {
        return repositories.getExtractedItemRepository();
    }

    protected abstract ExtractedItemRepository getExtractedItemRepository();

    @Inject
    private static Repositories repositories;

}
