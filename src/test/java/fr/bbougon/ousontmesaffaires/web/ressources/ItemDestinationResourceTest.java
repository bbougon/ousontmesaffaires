package fr.bbougon.ousontmesaffaires.web.ressources;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import fr.bbougon.ousontmesaffaires.command.WithCommandBus;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.infrastructure.security.Sha1Encryptor;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ItemDestinationResourceTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Rule
    public WithCommandBus withCommandBus = new WithCommandBus();

    @Before
    public void before() {
        containerResource = new ItemDestinationResource();
        withCommandBus.apply((CommandBus commandBus) -> containerResource.commandBus = commandBus);
    }

    @Test
    public void canMoveAContainerItemToExistingContainer() {
        Container container = Container.create("Container 1", Lists.newArrayList(Item.create("chaussure")));
        Container existingContainer = Container.create("Container 2", Lists.newArrayList(Item.create("chaussure2")));
        Repositories.containerRepository().persist(container);
        Repositories.containerRepository().persist(existingContainer);
        String itemHash = new Sha1Encryptor().cypher(new ItemStringFormatter(container.getItems().get(0)).format().getBytes());

        Response response = containerResource.destination(new Codec().urlSafeToBase64(container.getId().toString()), itemHash,
                "{\"destination\":\"" + new Codec().toBase64(existingContainer.getId().toString().getBytes()) + "\"}");

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(new Gson().toJson(response.getEntity())).isEqualTo("{\"id\":\"" + new Codec().urlSafeToBase64(existingContainer.getId().toString()) + "\",\"name\":\"Container 2\"," +
                "\"items\":[{\"item\":\"chaussure2\",\"imageStore\":{\"folder\":\"" + existingContainer.getItems().get(0).getImageStore().getFolder() + "\",\"images\":[]}," +
                "\"itemHash\":\"" + SecurityService.sha1().cypher(new ItemStringFormatter(existingContainer.getItems().get(0)).format().getBytes()) + "\",\"features\":[]}," +
                "{\"item\":\"chaussure\",\"imageStore\":{\"folder\":\"" + existingContainer.getItems().get(1).getImageStore().getFolder() + "\",\"images\":[]}," +
                "\"itemHash\":\"" + SecurityService.sha1().cypher(new ItemStringFormatter(existingContainer.getItems().get(1)).format().getBytes()) + "\",\"features\":[]}]}");
    }

    @Test
    public void checkPayloadWhenMovingToAnotherContainer() {
        try {
            containerResource.destination(new Codec().urlSafeToBase64(UUID.randomUUID().toString()), "unexisting hash", "{}");
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Payload cannot be empty.");
        }
    }

    private ItemDestinationResource containerResource;
}