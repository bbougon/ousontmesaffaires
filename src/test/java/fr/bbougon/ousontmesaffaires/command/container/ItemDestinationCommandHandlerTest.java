package fr.bbougon.ousontmesaffaires.command.container;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import fr.bbougon.ousontmesaffaires.command.Destination;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.container.FoundContainer;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.container.MovedItem;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.infrastructure.security.Sha1Encryptor;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ItemDestinationCommandHandlerTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Before
    public void before() {
        container = Container.create("Container name", Lists.newArrayList(Item.create("an item")));
        Repositories.containerRepository().persist(container);
    }

    @Test
    public void canMoveAnItemToAnExistingDestination() {
        ItemDestinationCommandHandler itemDestinationCommandHandler = new ItemDestinationCommandHandler();
        String itemHash = new Sha1Encryptor().cypher(new ItemStringFormatter(container.getItems().get(0)).format().getBytes());
        Container existingContainer = Container.create("Existing container", Lists.newArrayList(Item.create("existing Item")));
        Repositories.containerRepository().persist(existingContainer);
        String containerId = new Codec().toBase64(container.getId().toString().getBytes());

        final String payload = "{\"destination\":\"" + new Codec().toBase64(existingContainer.getId().toString().getBytes()) + "\"}";
        Pair<FoundContainer, Event> result = itemDestinationCommandHandler.execute(new ItemDestinationCommand(containerId, itemHash, new Gson().fromJson(payload, Destination.class)));

        assertThat(Repositories.containerRepository().getAll()).hasSize(2);
        assertThat(Repositories.containerRepository().get(container.getId()).get().getItems()).isEmpty();
        assertThat(existingContainer.getItems()).hasSize(2);
        assertThat(new Gson().toJson(result.getLeft())).isEqualTo("{\"id\":\"" + new Codec().urlSafeToBase64(existingContainer.getId().toString()) + "\",\"name\":\"Existing container\"," +
                "\"items\":[{\"item\":\"existing Item\",\"imageStore\":{\"folder\":\"" + existingContainer.getItems().get(0).getImageStore().getFolder() + "\",\"images\":[]}," +
                "\"itemHash\":\"" + SecurityService.sha1().cypher(new ItemStringFormatter(existingContainer.getItems().get(0)).format().getBytes()) + "\",\"features\":[]}," +
                "{\"item\":\"an item\",\"imageStore\":{\"folder\":\"" + existingContainer.getItems().get(1).getImageStore().getFolder() + "\",\"images\":[]}," +
                "\"itemHash\":\"" + SecurityService.sha1().cypher(new ItemStringFormatter(existingContainer.getItems().get(1)).format().getBytes()) + "\",\"features\":[]}]}");
        MovedItem movedItem = (MovedItem) result.getRight();
        assertThat(movedItem.getItemHash()).isEqualTo(itemHash);
    }

    @Test
    public void throwsBusinessErrorIfContainerDoesNotExist() {
        try {
            String base64UUID = new Codec().toBase64(UUID.randomUUID().toString().getBytes());
            new ItemDestinationCommandHandler().execute(new ItemDestinationCommand(base64UUID, "abcdef", new Destination(base64UUID)));
            failBecauseExceptionWasNotThrown(BusinessError.class);
        } catch (BusinessError e) {
            assertThat(e.getCode()).isEqualTo("UNEXISTING_CONTAINER");
        }
    }

    @Test
    public void throwsBusinessErrorIfDestinationContainerDoesNotExist() {
        try {
            Container existingContainer = Container.create("Existing container", Lists.newArrayList(Item.create("existing Item")));
            Repositories.containerRepository().persist(existingContainer);
            String containerId = new Codec().toBase64(container.getId().toString().getBytes());
            String base64UUID = new Codec().toBase64(UUID.randomUUID().toString().getBytes());
            new ItemDestinationCommandHandler().execute(new ItemDestinationCommand(containerId, "abcdef", new Destination(base64UUID)));
            failBecauseExceptionWasNotThrown(BusinessError.class);
        } catch (BusinessError e) {
            assertThat(e.getCode()).isEqualTo("UNEXISTING_DESTINATION_CONTAINER");
        }
    }

    private Container container;
}