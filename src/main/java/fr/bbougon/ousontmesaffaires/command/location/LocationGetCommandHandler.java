package fr.bbougon.ousontmesaffaires.command.location;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkTransaction;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class LocationGetCommandHandler implements CommandHandler<LocationGetCommand, String> {

    @MongolinkTransaction
    @Override
    public Pair<String, Object> execute(final LocationGetCommand locationGetCommand) {
        Location location = Repositories.locationRepository().findById(locationGetCommand.getUUID());
        String result = new GsonBuilder().create().toJson(
                location.toJsonObject(locationGetCommand.getId(), locationGetCommand.getQrCode()));
        return Pair.of(result, location);
    }

}
