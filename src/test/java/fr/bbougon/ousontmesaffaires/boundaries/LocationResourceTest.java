package fr.bbougon.ousontmesaffaires.boundaries;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.domain.location.*;
import fr.bbougon.ousontmesaffaires.repositories.memoire.LocationMemoryRepository;
import fr.bbougon.ousontmesaffaires.test.utils.JsonFileUtils;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

import static javax.ws.rs.core.Response.Status.CREATED;
import static org.fest.assertions.api.Assertions.assertThat;

public class LocationResourceTest {

    @Test
    public void canAddLocation() throws IOException {
        LocationResource locationResource = new LocationResource();
        locationResource.locationRepository = new LocationMemoryRepository();

        Response resource = locationResource.add(new JsonFileUtils("json/item.json").getPayload());

        assertThat(resource.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(resource.getLocation().getPath()).matches("^/location/[a-zA-Z0-9]{48}");
        List<Location> locations = locationResource.locationRepository.getAll();
        assertThat(locations).isNotNull();
        assertThat(locations.get(0).getItems()).isNotEmpty();
        assertThat(locations.get(0).getItems().get(0).getFeatures()).containsAll(Sets.newHashSet(
                new Feature(new Type("type"), "tshirt"),
                new Feature(new Type("couleur"), "blanc"),
                new Feature(new Type("taille"), "3ans")));
    }

}