package fr.bbougon.ousontmesaffaires.repositories.memoire;

import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.domain.location.LocationRepository;

import java.util.Optional;
import java.util.UUID;

public class LocationMemoryRepository extends MemoryRepository<Location> implements LocationRepository {

    @Override
    public Location findById(final UUID id) {
        Optional<Location> location = getAll().stream().filter(entity -> id.equals(entity.getId())).findFirst();
        return location.orElse(null);
    }
}
