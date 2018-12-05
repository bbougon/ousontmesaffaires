package fr.bbougon.ousontmesaffaires.saga.item;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.command.container.NLPCommand;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ContainerAddItemSagaHandlerTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Test
    public void canAddItemToContainer() {
        Container container = Container.create("New container", Lists.newArrayList(Item.create("old item")));
        Repositories.containerRepository().persist(container);

        String itemFromHash = new ContainerAddItemSagaHandler().run(commandBus, new ContainerAddItemSaga(container.getId(), "{\"item\": \"pantalon noir 3ans\"}"));

        assertThat(container.getItems()).hasSize(2);
        verify(commandBus).send(argThat(getValue(Lists.newArrayList(container.getItemFromHash(itemFromHash).get()), container.getId())));
    }

    @Test
    public void throwsBusinessErrorIfContainerNotFound() {
        try {
            new ContainerAddItemSagaHandler().run(commandBus, new ContainerAddItemSaga(UUID.randomUUID(), "{\"item\": \"pantalon noir 3ans\"}"));
            Assertions.failBecauseExceptionWasNotThrown(BusinessError.class);
        } catch (BusinessError e) {
            assertThat(e.getCode()).isEqualTo("UNEXISTING_CONTAINER");
        }

    }

    private ArgumentMatcher<NLPCommand> getValue(final List<Item> textToAnalyse, final UUID uuid) {
        return argument -> argument.itemsToAnalyse.containsAll(textToAnalyse) && argument.uuid.equals(uuid);
    }

    private CommandBus commandBus = mock(CommandBus.class);
}