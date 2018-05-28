package fr.bbougon.ousontmesaffaires.command.container;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Feature;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
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

import static org.assertj.core.api.Assertions.assertThat;

public class ItemDestinationCommandHandlerTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Before
    public void before() {
        container = Container.create("Container name",
                Item.create(Lists.newArrayList(Feature.create("type", "value"))));
        Repositories.containerRepository().persist(container);
    }

    @Test
    public void canMoveAContainerToANewDestination() {
        ItemDestinationCommandHandler itemDestinationCommandHandler = new ItemDestinationCommandHandler();
        String itemHash = new Sha1Encryptor().encrypt(new ItemStringFormatter(container.getItems().get(0)).format().getBytes());

        itemDestinationCommandHandler.execute(new ItemDestinationCommand(new Codec().toBase64(container.getId().toString().getBytes()), itemHash));

        assertThat(Repositories.containerRepository().getAll()).hasSize(2);
        assertThat(Repositories.containerRepository().findById(container.getId()).getItems()).isEmpty();
        Container newContainer = Repositories.containerRepository().getAll().get(1);
        assertThat(newContainer.getName()).isEqualTo("Holdall container");
        assertThat(newContainer.getDescription()).isEqualTo("Container containing all items that have been extracted from other containers");
        assertThat(newContainer.getItems()).hasSize(1);
        assertThat(newContainer.getItems().get(0).getFeatures()).contains(Feature.create("type", "value"));
    }

    @Test
    public void handlerReturnsAResult() {
        ItemDestinationCommandHandler itemDestinationCommandHandler = new ItemDestinationCommandHandler();
        String itemHash = new Sha1Encryptor().encrypt(new ItemStringFormatter(container.getItems().get(0)).format().getBytes());

        Pair<String, Object> result = itemDestinationCommandHandler.execute(new ItemDestinationCommand(new Codec().toBase64(container.getId().toString().getBytes()), itemHash));

        Container newContainer = Repositories.containerRepository().getAll().get(1);
        assertThat(result.getLeft()).isEqualTo(newContainer.getId().toString());
        assertThat(result.getRight()).isEqualTo(newContainer);
    }

    @Test
    public void handleUnexistingItem() {
        ItemDestinationCommandHandler itemDestinationCommandHandler = new ItemDestinationCommandHandler();

        Pair<String, Object> result = itemDestinationCommandHandler.execute(new ItemDestinationCommand(new Codec().toBase64(container.getId().toString().getBytes()), "unexistingHash"));

        assertThat(result.getLeft()).isNullOrEmpty();
        assertThat(result.getRight()).isNull();
    }

    private Container container;
}