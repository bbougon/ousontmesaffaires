package fr.bbougon.ousontmesaffaires.web.ressources;

import ch.qos.logback.classic.Level;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.command.CommandHandlersForTest;
import fr.bbougon.ousontmesaffaires.command.container.ContainerAddCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ContainerGetCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ContainerPatchCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ContainersGetCommandHandler;
import fr.bbougon.ousontmesaffaires.command.container.ItemAddToContainerCommandHandler;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Feature;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.module.transactional.TransactionalMiddleware;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGeneratorForTest;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.test.utils.TestAppender;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
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
        Repositories.containerRepository().persist(container);
        String containerId = new Codec().urlSafeToBase64(container.getId().toString());

        Response response = containerResource.getContainer(new UriInfoBuilderForTest().forContainer(containerId), containerId);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new FileUtilsForTest("json/expectedJsonResult.json").getContent()
                .replace("ID_TO_REPLACE", containerId)
                .replace("HASH_TO_REPLACE", "4f36b7a73c4ea3216fa6f989176f76777ff1e17c"));
    }

    @Test
    public void return404IfContainerNotFound() {
        Response response = containerResource.addItem(new Codec().urlSafeToBase64(UUID.randomUUID().toString()), new FileUtilsForTest("json/pantalon.json").getContent());

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    public void canGetAllContainers() {
        Container container1 = Container.create("Container 1", Item.create(Lists.newArrayList(Feature.create("type", "chaussure"))));
        Container container2 = Container.create("Container 2", Item.create(Lists.newArrayList(Feature.create("type", "pantalon"))));
        Repositories.containerRepository().persist(container1);
        Repositories.containerRepository().persist(container2);

        Response response = containerResource.getAll(new UriInfoBuilderForTest().forContainers());

        assertThat(response.getEntity()).isEqualTo(new FileUtilsForTest("json/expectedJsonsResult.json").getContent()
                .replace("ID_TO_REPLACE_1", new Codec().urlSafeToBase64(container1.getId().toString()))
                .replace("HASH_TO_REPLACE_1", "cb45c469548be4589275cd687f7d9f04680c5add")
                .replace("ID_TO_REPLACE_2", new Codec().urlSafeToBase64(container2.getId().toString()))
                .replace("HASH_TO_REPLACE_2", "b1957920397ff78af04c19f6361766863e03a0e2"));
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
    public void canAddContainerDescription() {
        Container container = Container.create("Container 1", Item.create(Lists.newArrayList(Feature.create("type", "chaussure"))));
        Repositories.containerRepository().persist(container);

        Response response = containerResource.patchContainer(new Codec().urlSafeToBase64(container.getId().toString()), "{\"fields\":[{\"fieldName\":\"description\",\"value\":\"A description\"}]}");

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(container.getDescription()).isEqualTo("A description");
    }

    private ContainerResource initialise(final Codec codec) {
        ContainerResource containerResource = new ContainerResource();
        containerResource.commandBus = new TransactionalMiddleware(new CommandHandlersForTest(Sets.newHashSet(
                new ContainerAddCommandHandler(),
                new ItemAddToContainerCommandHandler(),
                new ContainerGetCommandHandler(),
                new ContainersGetCommandHandler(),
                new ContainerPatchCommandHandler()
        )));
        containerResource.codec = codec;
        containerResource.qrGenerator = new QRGeneratorForTest();
        return containerResource;
    }

    private ContainerResource containerResource;
}
