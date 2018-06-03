package fr.bbougon.ousontmesaffaires.command.container;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
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
    public void canMoveAContainerToAnExistingDestination() {
        ItemDestinationCommandHandler itemDestinationCommandHandler = new ItemDestinationCommandHandler();
        String itemHash = new Sha1Encryptor().encrypt(new ItemStringFormatter(container.getItems().get(0)).format().getBytes());
        Container existingContainer = Container.create("Existing container", Item.create(Lists.newArrayList(Feature.create("existing Item", "existing value"))));
        Repositories.containerRepository().persist(existingContainer);
        String containerId = new Codec().toBase64(this.container.getId().toString().getBytes());

        final String payload = "{\"destination\":\"" + new Codec().toBase64(existingContainer.getId().toString().getBytes()) + "\"}";
        Pair<String, Object> result = itemDestinationCommandHandler.execute(
                new ItemDestinationCommand(containerId, itemHash,
                        new Gson().fromJson(payload, Destination.class)));

        assertThat(Repositories.containerRepository().getAll()).hasSize(2);
        assertThat(Repositories.containerRepository().findById(container.getId()).getItems()).isEmpty();
        assertThat(existingContainer.getItems()).hasSize(2);
        assertThat(result.getLeft()).isEqualTo(new GsonBuilder().create()
                .toJson(JsonMappers.fromContainer()
                        .map(existingContainer)));
        assertThat(result.getRight()).isEqualTo(existingContainer);
    }

    @Test
    public void handleUnexistingItem() {
        ItemDestinationCommandHandler itemDestinationCommandHandler = new ItemDestinationCommandHandler();
        Container existingContainer = Container.create("Existing container", Item.create(Lists.newArrayList(Feature.create("existing Item", "existing value"))));
        Repositories.containerRepository().persist(existingContainer);
        String containerId = new Codec().toBase64(container.getId().toString().getBytes());

        final String payload = "{\"destination\":\"" + new Codec().toBase64(existingContainer.getId().toString().getBytes()) + "\"}";
        Pair<String, Object> result = itemDestinationCommandHandler.execute(
                new ItemDestinationCommand(containerId, "unexistingHash",
                        new Gson().fromJson(payload, Destination.class)));

        assertThat(result.getLeft()).isNullOrEmpty();
        assertThat(result.getRight()).isNull();
    }

    private Container container;
}