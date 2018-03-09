package fr.bbougon.ousontmesaffaires.web.ressources;

import ch.qos.logback.classic.Level;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.command.CommandHandlersForTest;
import fr.bbougon.ousontmesaffaires.command.location.*;
import fr.bbougon.ousontmesaffaires.domain.location.*;
import fr.bbougon.ousontmesaffaires.infrastructure.module.transactional.TransactionalMiddleware;
import fr.bbougon.ousontmesaffaires.infrastructure.pdf.PdfGeneratorForTest;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGeneratorForTest;
import fr.bbougon.ousontmesaffaires.repositories.MemoryRepositories;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtils;
import fr.bbougon.ousontmesaffaires.test.utils.TestAppender;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import fr.bbougon.ousontmesaffaires.web.ressources.json.LocationName;
import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

public class LocationResourceTest {

    @Before
    public void before() {
        Repositories.initialise(new MemoryRepositories());
        locationResource = initialise(new Codec());
    }

    @Test
    public void canAddLocation() {
        Response response = locationResource.add(new FileUtils("json/t-shirt.json").getContent());

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(response.getLocation().getPath()).matches("^/locations/[a-zA-Z0-9]{48}");
        List<Location> locations = Repositories.locationRepository().getAll();
        assertThat(locations).isNotNull();
        assertThat(locations.get(0).getLocation()).isEqualTo("Cave");
        assertThat(locations.get(0).getItems()).isNotEmpty();
        assertThat(locations.get(0).getItems().get(0).getFeatures()).containsAll(Sets.newHashSet(
                Feature.create("type", "tshirt"),
                Feature.create("couleur", "blanc"),
                Feature.create("taille", "3ans")));
    }

    @Test
    public void canHandleUriErrorOnAddLocation() {
        locationResource.codec = new Codec() {
            @Override
            public String urlSafeToBase64(final String dataToEncode) {
                return "&&&&?&\";^%";
            }
        };

        Response response = locationResource.add(new FileUtils("json/t-shirt.json").getContent());

        assertThat(response.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(TestAppender.hasMessageInLevel(Level.WARN, "Error while building URI for path : " + LocationResource.PATH + "/&&&&?&\";^%")).isTrue();
    }

    @Test
    public void canAddAnItemToALocation() {
        String payload = "{\"location\":\"Cave\",\"item\":{\"type\":\"pantalon\"}}";
        Location location = Location.create(LocationName.getNameFromPayload(payload), Item.create(Features.getFeaturesFromPayload(payload)));
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
        String payload = "{\"location\": \"Bureau\",\"item\": {\"type\": \"tshirt\",\"couleur\": \"blanc\",\"taille\": \"3ans\"}}";
        Location location = Location.create(LocationName.getNameFromPayload(payload), Item.create(Features.getFeaturesFromPayload(payload)));
        Repositories.locationRepository().persist(location);
        String locationId = new Codec().urlSafeToBase64(location.getId().toString());

        Response response = locationResource.getLocation(new ResteasyUriInfo(new URIBuilder().build("http://locahost")), locationId);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new FileUtils("json/expectedJsonResult.json").getContent().replace("ID_TO_REPLACE", locationId));
    }

    @Test
    public void return404IfLocationNotFound() {
        Response response = locationResource.addItem(new Codec().urlSafeToBase64(UUID.randomUUID().toString()), new FileUtils("json/pantalon.json").getContent());

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    public void canGetAllLocations() {
        Location location1 = Location.create("Location 1", Item.create(Lists.newArrayList(Feature.create("type", "chaussure"))));
        Location location2 = Location.create("Location 2", Item.create(Lists.newArrayList(Feature.create("type", "pantalon"))));
        Repositories.locationRepository().persist(location1);
        Repositories.locationRepository().persist(location2);

        Response response = locationResource.getAll();

        assertThat(response.getEntity()).isEqualTo(new FileUtils("json/expectedJsonsResult.json").getContent()
                .replace("ID_TO_REPLACE_1", new Codec().urlSafeToBase64(location1.getId().toString()))
                .replace("ID_TO_REPLACE_2", new Codec().urlSafeToBase64(location2.getId().toString())));
    }

    @Test
    public void checkPayloadIsNotNullWhenAddingLocation() {
        try {
            locationResource.add(null);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException exception) {
            assertThat(exception.getMessage()).isEqualTo("Payload cannot be null.");
        }
    }

    @Test
    public void checkPayloadIsNotEmptyWhenAddingLocation() {
        try {
            locationResource.add("   ");
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException exception) {
            assertThat(exception.getMessage()).isEqualTo("Payload cannot be empty.");
        }
    }

    @Test
    public void checkPayloadIsNotNullWhenAddingItemToLocation() {
        try {
            locationResource.addItem("id", null);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException exception) {
            assertThat(exception.getMessage()).isEqualTo("Payload cannot be null.");
        }
    }

    @Test
    public void checkPayloadIsNotEmptyWhenAddingItemToLocation() {
        try {
            locationResource.addItem("id", "  ");
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException exception) {
            assertThat(exception.getMessage()).isEqualTo("Payload cannot be empty.");
        }
    }

    @Test
    public void canGeneratePdfWithSticker() {
        Location location = Location.create("Location 1", Item.create(Lists.newArrayList(Feature.create("type", "chaussure"))));
        Repositories.locationRepository().persist(location);
        String locationId = new Codec().urlSafeToBase64(location.getId().toString());

        Response response = locationResource.generateStickers(locationId);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new File("src/test/resources/file/expected.pdf"));
        assertThat(response.getHeaderString("Content-Disposition")).isEqualTo("filename=expected.pdf");
    }

    private LocationResource initialise(final Codec codec) {
        LocationResource locationResource = new LocationResource();
        locationResource.commandBus = new TransactionalMiddleware(new CommandHandlersForTest(Sets.newHashSet(
                new LocationAddCommandHandler(),
                new ItemAddToLocationCommandHandler(),
                new LocationGetCommandHandler(),
                new LocationsGetCommandHandler(),
                new GenerateStickersCommandHandler(new PdfGeneratorForTest())
        )));
        locationResource.codec = codec;
        locationResource.qrGenerator = new QRGeneratorForTest();
        return locationResource;
    }

    private LocationResource locationResource;
}
