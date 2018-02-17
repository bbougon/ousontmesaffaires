package fr.bbougon.ousontmesaffaires.repositories;

import fr.bbougon.ousontmesaffaires.domain.location.LocationRepository;
import fr.bbougon.ousontmesaffaires.repositories.memoire.LocationMemoryRepository;

public class MemoryRepositories extends Repositories {
    @Override
    protected LocationRepository getLocationRepository() {
        return locationMemoryRepository;
    }

    private final LocationMemoryRepository locationMemoryRepository = new LocationMemoryRepository();
}
