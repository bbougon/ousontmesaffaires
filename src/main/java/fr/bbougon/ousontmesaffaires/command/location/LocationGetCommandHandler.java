package fr.bbougon.ousontmesaffaires.command.location;

import com.google.gson.*;
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
        JsonArray items = new JsonArray();
        location.getItems().forEach(item -> {
            JsonObject itemJson = new JsonObject();
            JsonObject featureJson = new JsonObject();
            item.getFeatures().forEach(feature -> featureJson.addProperty(feature.getType(), feature.getFeature()));
            itemJson.add("item", featureJson);
            items.add(itemJson);
        });
        JsonObject locationJson = new JsonObject();
        locationJson.addProperty("id", locationGetCommand.getId());
        locationJson.addProperty("location", location.getLocation());
        locationJson.add("items", items);
        locationJson.addProperty("qrcode", locationGetCommand.getQrCode());
        String result = new GsonBuilder().create().toJson(locationJson);
        return Pair.of(result, location);
    }

}
