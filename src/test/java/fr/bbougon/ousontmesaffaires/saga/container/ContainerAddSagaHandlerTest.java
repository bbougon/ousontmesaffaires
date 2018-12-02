package fr.bbougon.ousontmesaffaires.saga.container;

import fr.bbougon.ousontmesaffaires.command.container.NLPCommand;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ContainerAddSagaHandlerTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Test
    public void nlpServicesIsCalled() {
        CommandBus commandBus = mock(CommandBus.class);
        UUID uuid = new ContainerAddSagaHandler().run(commandBus, new ContainerAddSaga(new FileUtilsForTest("json/pantalon.json").getContent()));

        Optional<Container> container = Repositories.containerRepository().get(uuid);
        verify(commandBus).send(argThat(getValue(container.get().getItems(), uuid)));
    }

    private ArgumentMatcher<NLPCommand> getValue(final List<Item> textToAnalyse, final UUID uuid) {
        return argument -> argument.itemsToAnalyse.containsAll(textToAnalyse) && argument.uuid.equals(uuid);
    }
}