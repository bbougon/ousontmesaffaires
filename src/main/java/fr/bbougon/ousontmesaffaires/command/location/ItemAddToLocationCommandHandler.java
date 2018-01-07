package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.repositories.LocationRepository;
import fr.bbougon.ousontmesaffaires.repositories.MongoConfiguration;
import org.apache.commons.lang3.tuple.Pair;

public class ItemAddToLocationCommandHandler implements CommandHandler<ItemAddToLocationCommand, Nothing> {

    public ItemAddToLocationCommandHandler(final LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Pair<Nothing, Object> execute(final ItemAddToLocationCommand itemAddToLocationCommand) {
        MongoConfiguration.startSession();
        Location location = locationRepository.findById(itemAddToLocationCommand.getUuid());
        if(location == null) {
            return null;
        }
        location.add(itemAddToLocationCommand.getItem());
        MongoConfiguration.stopSession();
        return Pair.of(Nothing.INSTANCE, location);
    }

    private final LocationRepository locationRepository;
}
