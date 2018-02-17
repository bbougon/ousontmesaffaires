package fr.bbougon.ousontmesaffaires.command.location;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class LocationsGetCommandHandler implements CommandHandler<LocationsGetCommand, String> {

    @Override
    public Pair<String, Object> execute(final LocationsGetCommand locationsGetCommand) {
        List<Location> locations = Repositories.locationRepository().getAll();
        return Pair.of(new GsonBuilder().create().toJson(getJsonElements(locationsGetCommand, locations)), Nothing.INSTANCE);
    }

    private JsonArray getJsonElements(final LocationsGetCommand locationGetCommand, final List<Location> locations) {
        JsonArray jsonArray = new JsonArray();
        locations.stream()
                .map(location -> location.toJsonObject(
                        locationGetCommand.getId(location.getId()), locationGetCommand.getQrCode(location.getId())))
                .collect(Collectors.toList())
                .forEach(jsonArray::add);
        return jsonArray;
    }

}
