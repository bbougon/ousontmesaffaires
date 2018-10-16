package fr.bbougon.ousontmesaffaires.command.container;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import fr.bbougon.ousontmesaffaires.command.Patch;
import fr.bbougon.ousontmesaffaires.command.PatchException;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
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
        Patch patch = new Gson().fromJson("{\"target\":\"description\",\"id\":\"\",\"version\":1,\"data\":\"A description\"}", Patch.class);

        Pair<String, Object> result = new ContainerPatchCommandHandler().execute(new ContainerPatchCommand(new Codec().toBase64(container.getId().toString().getBytes()), patch));

        Container container = (Container) result.getRight();
        assertThat(container.getDescription()).isEqualTo("A description");
    }

    @Test
    public void canPatchItem() {
        String itemHash = new Sha1Encryptor().cypher(new ItemStringFormatter(container.getItems().get(0)).format().getBytes());
        String jsonPatch = new FileUtilsForTest("json/itemPatch.json").getContent().replace("HASH_TO_REPLACE", itemHash);
        Patch patch = new Gson().fromJson(jsonPatch, Patch.class);

        Pair<String, Object> result = new ContainerPatchCommandHandler().execute(new ContainerPatchCommand(new Codec().toBase64(container.getId().toString().getBytes()), patch));

        Container container = (Container) result.getRight();
        assertThat(container.getItems().get(0).getImages()).hasSize(1);
        assertThat(container.getItems().get(0).getImageStore().getFolder()).matches("[a-zA-Z0-9]{48}");
    }

    @Test
    public void canHandleExceptions() {
        try {
            Patch patch = new Gson().fromJson("{\"target\":\"unknown\",\"id\":\"\",\"version\":1,\"data\":\"A description\"}", Patch.class);
            new ContainerPatchCommandHandler().execute(new ContainerPatchCommand(new Codec().toBase64(container.getId().toString().getBytes()), patch));
            failBecauseExceptionWasNotThrown(PatchException.class);
        } catch (PatchException e) {
            assertThat(e.getMessage()).contains("An error occurred during patch, current target 'unknown' is unknown.");
        }
    }

    private Container container;
}