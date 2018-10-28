package fr.bbougon.ousontmesaffaires.command.container;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.container.FoundContainer;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.container.PatchedContainer;
import fr.bbougon.ousontmesaffaires.infrastructure.security.Sha1Encryptor;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ContainerPatchCommandHandlerTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Before
    public void before() {
        container = Container.create("Container name", Lists.newArrayList(Item.create(" an item")));
        Repositories.containerRepository().persist(container);
    }

    @Test
    public void canPatchDescription() {
        String json = "{\"target\":\"item.description\",\"id\":\"\",\"version\":1,\"data\":\"A description\"}";

        Pair<FoundContainer, NextEvent> result = new ContainerPatchCommandHandler().execute(new ContainerPatchCommand(new Codec().toBase64(container.getId().toString().getBytes()), json));

        FoundContainer container = result.getLeft();
        assertThat(container.description).isEqualTo("A description");
    }

    @Test
    public void canPatchItem() {
        String itemHash = new Sha1Encryptor().cypher(new ItemStringFormatter(container.getItems().get(0)).format().getBytes());
        String jsonPatch = new FileUtilsForTest("json/itemImagePatch.json").getContent().replace("HASH_TO_REPLACE", itemHash);

        Pair<FoundContainer, NextEvent> result = new ContainerPatchCommandHandler().execute(new ContainerPatchCommand(new Codec().toBase64(container.getId().toString().getBytes()), jsonPatch));

        FoundContainer container = result.getLeft();
        assertThat(container.items.get(0).item).isEqualTo(" an item");
        assertThat(container.items.get(0).imageStore.images).hasSize(1);
        assertThat(container.items.get(0).imageStore.folder).matches("[a-zA-Z0-9]{48}");
    }

    @Test
    public void throwsBusinessErrorIfContainerNotFound() {
        try {
            String json = "{\"target\":\"unknown\",\"id\":\"\",\"version\":1,\"data\":\"A description\"}";
            new ContainerPatchCommandHandler().execute(new ContainerPatchCommand(new Codec().toBase64(UUID.randomUUID().toString().getBytes()), json));
            failBecauseExceptionWasNotThrown(BusinessError.class);
        } catch (BusinessError e) {
            assertThat(e.getCode()).isEqualTo("UNEXISTING_CONTAINER");
        }
    }

    @Test
    public void canHandleExceptions() {
        try {
            String json = "{\"target\":\"unknown\",\"id\":\"\",\"version\":1,\"data\":\"A description\"}";
            new ContainerPatchCommandHandler().execute(new ContainerPatchCommand(new Codec().toBase64(container.getId().toString().getBytes()), json));
            failBecauseExceptionWasNotThrown(BusinessError.class);
        } catch (BusinessError e) {
            assertThat(e.getMessage()).isEqualTo("UNKNOWN_PATCH");
        }
    }

    private Container container;
}