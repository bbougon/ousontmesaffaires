package fr.bbougon.ousontmesaffaires.web.ressources;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.command.WithCommandBus;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ContainerAddItemResourceTest {

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
    public void canAddAnItemToAContainer() {
        Container container = Container.create("Cave", Lists.newArrayList(Item.create("pantalon")));
        Repositories.containerRepository().persist(container);

        Response response = containerResource.addItem(new Codec().urlSafeToBase64(container.getId().toString()), new FileUtilsForTest("json/pantalon.json").getContent());

        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
        List<Container> containers = Repositories.containerRepository().getAll();
        assertThat(containers.get(0).getItems()).hasSize(2);
        assertThat(containers.get(0).getItems().get(1).getItem()).isEqualTo("pantalon noir 3ans");
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
    public void checkPayloadIsNotNullWhenAddingItemToContainer() {
        try {
            containerResource.addItem("id", null);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException exception) {
            assertThat(exception.getMessage()).isEqualTo("Payload cannot be null.");
        }
    }

    @Test(expected = BusinessError.class)
    public void throwsBusinessErrorIfContainerNotFound() {
        containerResource.addItem(new Codec().urlSafeToBase64(UUID.randomUUID().toString()), new FileUtilsForTest("json/pantalon.json").getContent());
    }

    private ContainerAddItemResource initialise(final Codec codec) {
        ContainerAddItemResource containerResource = new ContainerAddItemResource();
        withCommandBus.apply((CommandBus commandBus) -> containerResource.commandBus = commandBus);
        containerResource.codec = codec;
        return containerResource;
    }

    private ContainerAddItemResource containerResource;
}