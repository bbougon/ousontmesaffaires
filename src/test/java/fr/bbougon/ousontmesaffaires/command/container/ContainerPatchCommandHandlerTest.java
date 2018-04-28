package fr.bbougon.ousontmesaffaires.command.container;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.command.Patch;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Feature;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContainerPatchCommandHandlerTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Before
    public void before() {
        container = Container.create("Container name",
                Item.create(Lists.newArrayList(Feature.create("type", "value"))));
        Repositories.containerRepository().persist(container);
    }

    @Test
    public void canPatchDescription() {
        Patch patch = new Patch(Lists.newArrayList(new Field("description", "A description")));

        Pair<String, Object> result = new ContainerPatchCommandHandler().execute(new ContainerPatchCommand(new Codec().toBase64(container.getId().toString().getBytes()), patch));

        Container container = (Container) result.getRight();
        assertThat(container.getDescription()).isEqualTo("A description");
    }

    private Container container;
}