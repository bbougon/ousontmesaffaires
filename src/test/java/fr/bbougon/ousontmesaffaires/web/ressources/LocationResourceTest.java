package fr.bbougon.ousontmesaffaires.web.ressources;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.command.CommandHandlersForTest;
import fr.bbougon.ousontmesaffaires.domain.location.*;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGeneratorForTest;
import fr.bbougon.ousontmesaffaires.repositories.MemoryRepositories;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtils;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.*;
import static org.fest.assertions.api.Assertions.assertThat;

public class LocationResourceTest {

    @Before
    public void before() {
        Repositories.initialise(new MemoryRepositories());
        locationResource = initialise();
    }

    @Test
    public void canAddLocation() throws IOException {
        Response response = locationResource.add(new FileUtils("json/t-shirt.json").getContent());

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(response.getLocation().getPath()).matches("^/location/[a-zA-Z0-9]{48}");
        List<Location> locations = Repositories.locationRepository().getAll();
        assertThat(locations).isNotNull();
        assertThat(locations.get(0).getItems()).isNotEmpty();
        assertThat(locations.get(0).getItems().get(0).getFeatures()).containsAll(Sets.newHashSet(
                Feature.create("type", "tshirt"),
                Feature.create("couleur", "blanc"),
                Feature.create("taille", "3ans")));
    }

    @Test
    public void canAddAnItemToALocation() {
        Location location = new Location();
        location.add(Item.create(Features.getFeaturesFromPayload("{\"item\":{\"type\":\"pantalon\"}}")));
        Repositories.locationRepository().persist(location);

        Response response = locationResource.addItem(new Codec().urlSafeToBase64(location.getId().toString()), new FileUtils("json/pantalon.json").getContent());

        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
        List<Location> locations = Repositories.locationRepository().getAll();
        assertThat(locations.get(0).getItems()).hasSize(2);
        assertThat(locations.get(0).getItems().get(1).getFeatures()).containsAll(Sets.newHashSet(
                Feature.create("type", "pantalon"),
                Feature.create("couleur", "noir"),
                Feature.create("taille", "3ans")));
    }

    @Test
    public void canGetALocation() {
        Location location = new Location();
        location.add(Item.create(Features.getFeaturesFromPayload("{\"item\": {\"type\": \"tshirt\",\"couleur\": \"blanc\",\"taille\": \"3ans\"}}")));
        Repositories.locationRepository().persist(location);

        Response response = locationResource.getLocation(new ResteasyUriInfo(new URIBuilder().build("http://locahost")), new Codec().urlSafeToBase64(location.getId().toString()));

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new FileUtils("json/expectedJsonResult.json").getContent());
    }

    @Test
    public void return404IfLocationNotFound() {
        Response response = locationResource.addItem(new Codec().urlSafeToBase64(UUID.randomUUID().toString()), new FileUtils("json/pantalon.json").getContent());

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    private LocationResource initialise() {
        LocationResource locationResource = new LocationResource();
        locationResource.commandHandlers = new CommandHandlersForTest();
        locationResource.codec = new Codec();
        locationResource.qrGenerator = new QRGeneratorForTest();
        return locationResource;
    }

    private LocationResource locationResource;
}
