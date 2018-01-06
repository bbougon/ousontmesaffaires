package fr.bbougon.ousontmesaffaires.boundaries;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.domain.location.*;
import fr.bbougon.ousontmesaffaires.repositories.memoire.LocationMemoryRepository;
import fr.bbougon.ousontmesaffaires.test.utils.JsonFileUtils;
import fr.bbougon.ousontmesaffaires.web.helpers.Encoder;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ItemJSON;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static org.fest.assertions.api.Assertions.assertThat;

public class LocationResourceTest {

    @Before
    public void before() {
        locationResource = initialise();
    }

    @Test
    public void canAddLocation() throws IOException {
        Response response = locationResource.add(new JsonFileUtils("json/t-shirt.json").getPayload());

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(response.getLocation().getPath()).matches("^/location/[a-zA-Z0-9]{48}");
        List<Location> locations = locationResource.locationRepository.getAll();
        assertThat(locations).isNotNull();
        assertThat(locations.get(0).getItems()).isNotEmpty();
        assertThat(locations.get(0).getItems().get(0).getFeatures()).containsAll(Sets.newHashSet(
                new Feature(new Type("type"), "tshirt"),
                new Feature(new Type("couleur"), "blanc"),
                new Feature(new Type("taille"), "3ans")));
    }

    @Test
    public void canAddAnItemToALocation() {
        Location location = new Location();
        location.add(Item.create(ItemJSON.from("{\"item\":{\"type\":\"pantalon\"}}")));
        locationResource.locationRepository.persist(location);

        Response response = locationResource.addItem(Encoder.toBase64(location.getId().toString()), new JsonFileUtils("json/pantalon.json").getPayload());

        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
        List<Location> locations = locationResource.locationRepository.getAll();
        assertThat(locations.get(0).getItems()).hasSize(2);
        assertThat(locations.get(0).getItems().get(1).getFeatures()).containsAll(Sets.newHashSet(
                new Feature(new Type("type"), "pantalon"),
                new Feature(new Type("couleur"), "noir"),
                new Feature(new Type("taille"), "3ans")));
    }

    @Test
    public void return404IfLocationNotFound() {
        Response response = locationResource.addItem(Encoder.toBase64(UUID.randomUUID().toString()), new JsonFileUtils("json/pantalon.json").getPayload());

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    private LocationResource initialise() {
        LocationResource locationResource = new LocationResource();
        locationResource.locationRepository = new LocationMemoryRepository();
        return locationResource;
    }

    private LocationResource locationResource;
}
