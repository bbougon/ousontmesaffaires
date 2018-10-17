package fr.bbougon.ousontmesaffaires.web.ressources;

import ch.qos.logback.classic.Level;
import fr.bbougon.ousontmesaffaires.command.WithCommandBus;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.test.utils.TestAppender;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ContainerAddResourceTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Rule
    public WithCommandBus withCommandBus = new WithCommandBus();

    @Before
    public void before() {
        containerAddResource = new ContainerAddResource();
        containerAddResource.codec = new Codec();
        withCommandBus.apply((CommandBus commandBus) -> containerAddResource.commandBus = commandBus);
    }

    @Test
    public void canAddContainer() {
        Response response = containerAddResource.add(new FileUtilsForTest("json/t-shirt.json").getContent());

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(response.getLocation().getPath()).matches("^/containers/[a-zA-Z0-9]{48}");
        List<Container> containers = Repositories.containerRepository().getAll();
        assertThat(containers).isNotNull();
        assertThat(containers.get(0).getName()).isEqualTo("Cave");
        assertThat(containers.get(0).getItems()).isNotEmpty();
        assertThat(containers.get(0).getItems().get(0).getItem()).isEqualTo("tshirt blanc 3ans");
    }

    @Test
    public void canHandleUriErrorOnAddContainer() {
        containerAddResource.codec = new Codec() {
            @Override
            public String urlSafeToBase64(final String dataToEncode) {
                return "&&&&?&\";^%";
            }
        };

        Response response = containerAddResource.add(new FileUtilsForTest("json/t-shirt.json").getContent());

        assertThat(response.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(TestAppender.hasMessageInLevel(Level.WARN, "Error while building URI for path : " + "/containers" + "/&&&&?&\";^%")).isTrue();
    }

    @Test
    public void checkPayloadIsNotEmptyWhenAddingContainer() {
        try {
            new ContainerAddResource().add("   ");
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException exception) {
            assertThat(exception.getMessage()).isEqualTo("Payload cannot be empty.");
        }
    }

    @Test
    public void checkPayloadIsNotNullWhenAddingContainer() {
        try {
            new ContainerAddResource().add(null);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException exception) {
            assertThat(exception.getMessage()).isEqualTo("Payload cannot be null.");
        }
    }

    private ContainerAddResource containerAddResource;
}