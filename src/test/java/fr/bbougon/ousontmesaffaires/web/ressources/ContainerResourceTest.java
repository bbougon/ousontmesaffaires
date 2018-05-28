package fr.bbougon.ousontmesaffaires.web.ressources;

import ch.qos.logback.classic.Level;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.WithCommandBus;
import fr.bbougon.ousontmesaffaires.command.container.ContainerField;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Feature;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.container.image.Image;
import fr.bbougon.ousontmesaffaires.domain.container.image.ResizedImage;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGeneratorForTest;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.infrastructure.security.Sha1Encryptor;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.test.utils.TestAppender;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ContainerName;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ContainerResourceTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Rule
    public WithCommandBus withCommandBus = new WithCommandBus();

    @Before
    public void before() {
        containerResource = initialise(new Codec());
    }

    @Test
    public void canAddContainer() {
        Response response = containerResource.add(new FileUtilsForTest("json/t-shirt.json").getContent());

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(response.getLocation().getPath()).matches("^/containers/[a-zA-Z0-9]{48}");
        List<Container> containers = Repositories.containerRepository().getAll();
        assertThat(containers).isNotNull();
        assertThat(containers.get(0).getName()).isEqualTo("Cave");
        assertThat(containers.get(0).getItems()).isNotEmpty();
        assertThat(containers.get(0).getItems().get(0).getFeatures()).containsAll(Sets.newHashSet(
                Feature.create("type", "tshirt"),
                Feature.create("couleur", "blanc"),
                Feature.create("taille", "3ans")));
    }

    @Test
    public void canHandleUriErrorOnAddContainer() {
        containerResource.codec = new Codec() {
            @Override
            public String urlSafeToBase64(final String dataToEncode) {
                return "&&&&?&\";^%";
            }
        };

        Response response = containerResource.add(new FileUtilsForTest("json/t-shirt.json").getContent());

        assertThat(response.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(TestAppender.hasMessageInLevel(Level.WARN, "Error while building URI for path : " + ContainerResource.PATH + "/&&&&?&\";^%")).isTrue();
    }

    @Test
    public void canAddAnItemToAContainer() {
        String payload = "{\"name\":\"Cave\",\"item\":{\"type\":\"pantalon\"}}";
        Container container = Container.create(ContainerName.getNameFromPayload(payload), Item.create(Features.getFeaturesFromPayload(payload)));
        Repositories.containerRepository().persist(container);

        Response response = containerResource.addItem(new Codec().urlSafeToBase64(container.getId().toString()), new FileUtilsForTest("json/pantalon.json").getContent());

        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
        List<Container> containers = Repositories.containerRepository().getAll();
        assertThat(containers.get(0).getItems()).hasSize(2);
        assertThat(containers.get(0).getItems().get(1).getFeatures()).containsAll(Sets.newHashSet(
                Feature.create("type", "pantalon"),
                Feature.create("couleur", "noir"),
                Feature.create("taille", "3ans")));
    }

    @Test
    public void canGetAContainer() {
        String payload = "{\"name\": \"Bureau\",\"item\": {\"type\": \"tshirt\",\"couleur\": \"blanc\",\"taille\": \"3ans\"}}";
        Container container = Container.create(ContainerName.getNameFromPayload(payload), Item.create(Features.getFeaturesFromPayload(payload)));
        addImagesToContainer(container);
        Repositories.containerRepository().persist(container);
        String containerId = new Codec().urlSafeToBase64(container.getId().toString());

        Response response = containerResource.getContainer(new UriInfoBuilderForTest().forContainer(containerId), containerId);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new FileUtilsForTest("json/expectedJsonResult.json").getContent()
                .replace("ID_TO_REPLACE", containerId)
                .replace("FOLDER_NAME", container.getItems().get(0).getImageStore().getFolder())
                .replace("HASH_TO_REPLACE", SecurityService.sha1().encrypt(new FileUtilsForTest("test/expectedSingleFormattedItem.txt")
                        .getContent()
                        .replace("FOLDER_NAME", container.getItems().get(0).getImageStore().getFolder()).getBytes())));
    }

    @Test
    public void return404IfContainerNotFound() {
        Response response = containerResource.addItem(new Codec().urlSafeToBase64(UUID.randomUUID().toString()), new FileUtilsForTest("json/pantalon.json").getContent());

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    public void canGetAllContainers() {
        Container container1 = Container.create("Container 1", Item.create(Lists.newArrayList(Feature.create("type", "chaussure"))));
        addImagesToContainer(container1);
        Container container2 = Container.create("Container 2", Item.create(Lists.newArrayList(Feature.create("type", "pantalon"))));
        addImagesToContainer(container2);
        Repositories.containerRepository().persist(container1);
        Repositories.containerRepository().persist(container2);

        Response response = containerResource.getAll(new UriInfoBuilderForTest().forContainers());

        assertThat(response.getEntity()).isEqualTo(new FileUtilsForTest("json/expectedJsonsResult.json").getContent()
                .replace("ID_TO_REPLACE_1", new Codec().urlSafeToBase64(container1.getId().toString()))
                .replace("FOLDER_NAME_1", container1.getItems().get(0).getImageStore().getFolder())
                .replace("HASH_TO_REPLACE_1", SecurityService.sha1().encrypt(new FileUtilsForTest("test/expectedFormattedItem.txt")
                        .getContent()
                        .replace("FOLDER_NAME", container1.getItems().get(0).getImageStore().getFolder()).getBytes()))
                .replace("ID_TO_REPLACE_2", new Codec().urlSafeToBase64(container2.getId().toString()))
                .replace("FOLDER_NAME_2", container2.getItems().get(0).getImageStore().getFolder())
                .replace("HASH_TO_REPLACE_2", SecurityService.sha1().encrypt(new FileUtilsForTest("test/expectedSecondFormattedItem.txt")
                        .getContent()
                        .replace("FOLDER_NAME", container2.getItems().get(0).getImageStore().getFolder()).getBytes())));
    }

    private void addImagesToContainer(final Container container) {
        container.getItems().get(0).add(Image.create("signature", "url", "secureUrl",
                Lists.newArrayList(
                        ResizedImage.create("url2", "secureUrl2", 100, 200),
                        ResizedImage.create("url3", "secureUrl3", 200, 400))
        ));
        container.getItems().get(0).add(Image.create("signature2", "url4", "secureUrl4",
                Lists.newArrayList(
                        ResizedImage.create("url5", "secureUrl5", 100, 200),
                        ResizedImage.create("url6", "secureUrl6", 200, 400))
        ));
    }

    @Test
    public void checkPayloadIsNotNullWhenAddingContainer() {
        try {
            containerResource.add(null);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException exception) {
            assertThat(exception.getMessage()).isEqualTo("Payload cannot be null.");
        }
    }

    @Test
    public void checkPayloadIsNotEmptyWhenAddingContainer() {
        try {
            containerResource.add("   ");
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException exception) {
            assertThat(exception.getMessage()).isEqualTo("Payload cannot be empty.");
        }
    }

    @Test
    public void checkPayloadIsNotNullWhenAddingItemToContainer() {
        try {
            containerResource.addItem("id", null);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException exception) {
            assertThat(exception.getMessage()).isEqualTo("Payload cannot be null.");
        }
    }

    @Test
    public void checkPayloadIsNotEmptyWhenAddingItemToContainer() {
        try {
            containerResource.addItem("id", "  ");
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException exception) {
            assertThat(exception.getMessage()).isEqualTo("Payload cannot be empty.");
        }
    }

    @Test
    public void canPatchAContainer() {
        Container container = Container.create("Container 1", Item.create(Lists.newArrayList(Feature.create("type", "chaussure"))));
        Repositories.containerRepository().persist(container);

        Response response = containerResource.patchContainer(new Codec().urlSafeToBase64(container.getId().toString()), "{\"target\":\"description\",\"id\":\"\",\"version\":1,\"data\":\"A description\"}");

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new GsonBuilder()
                .create()
                .toJson(JsonMappers.fromContainer().map(container, new ContainerField(new Codec().urlSafeToBase64(container.getId().toString())))));
    }

    @Test
    public void canMoveAContainerItemToANewContainer() {
        Container container = Container.create("Container 1", Item.create(Lists.newArrayList(Feature.create("type", "chaussure"))));
        Repositories.containerRepository().persist(container);
        String itemHash = new Sha1Encryptor().encrypt(new ItemStringFormatter(container.getItems().get(0)).format().getBytes());

        Response response = containerResource.destination(new Codec().urlSafeToBase64(container.getId().toString()), itemHash, "{}");

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(response.getLocation().getPath()).matches("^/containers/[a-zA-Z0-9]{48}");
    }

    @Test
    public void returns404OnUnexistingItem() {
        Container container = Container.create("Container 1", Item.create(Lists.newArrayList(Feature.create("type", "chaussure"))));
        Repositories.containerRepository().persist(container);

        Response response = containerResource.destination(new Codec().urlSafeToBase64(container.getId().toString()), "unexisting hash", "{}");

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    public void returns404OnUnexistingContainer() {
        Response response = containerResource.destination(new Codec().urlSafeToBase64(UUID.randomUUID().toString()), "unexisting hash", "{}");

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    private ContainerResource initialise(final Codec codec) {
        ContainerResource containerResource = new ContainerResource();
        withCommandBus.apply((CommandBus commandBus) -> containerResource.commandBus = commandBus);
        containerResource.codec = codec;
        containerResource.qrGenerator = new QRGeneratorForTest();
        return containerResource;
    }

    private ContainerResource containerResource;
}
