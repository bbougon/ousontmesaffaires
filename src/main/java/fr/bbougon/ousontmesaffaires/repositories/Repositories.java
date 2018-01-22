package fr.bbougon.ousontmesaffaires.repositories;

import javax.inject.Inject;

public abstract class Repositories {

    public static void initialise(Repositories repositories) {
        Repositories.repositories = repositories;
    }

    public static LocationRepository locationRepository() {
        return repositories.getLocationRepository();
    }

    protected abstract LocationRepository getLocationRepository();

    @Inject
    private static Repositories repositories;

}
