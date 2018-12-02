package fr.bbougon.ousontmesaffaires.command.container;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.container.NLPAnalyzedItem;
import fr.bbougon.ousontmesaffaires.rules.WithFakeServices;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.test.utils.NLPAnalysisBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class NLPCommandHandlerTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Rule
    public WithFakeServices withFakeServices = new WithFakeServices();

    @Test
    public void canAnalyzeItem() {
        withFakeServices.getFakeService().nlpServiceReturns(Lists.newArrayList(new NLPAnalysisBuilder()
                .withDefaultEntitiesAnalysis()
                .withDefaultCategories()
                .withDefaultConcepts()
                .build()));
        Container container = Container.create("a container", Lists.newArrayList(Item.create("an item")));
        Repositories.containerRepository().persist(container);

        Pair<List<NLPAnalyzedItem>, Event> result = new NLPCommandHandler().execute(new NLPCommand(container.getItems(), container.getId()));

        assertThat(result.getLeft()).hasSize(1);
        assertThat(result.getRight()).isEqualTo(Nothing.INSTANCE);
    }

    @Test
    public void throwBusinessErrorOnUnknownContainer() {
        try {
            new NLPCommandHandler().execute(new NLPCommand(Lists.newArrayList(), UUID.randomUUID()));
            failBecauseExceptionWasNotThrown(BusinessError.class);
        } catch (BusinessError e) {
            assertThat(e.getCode()).isEqualTo("UNEXISTING_CONTAINER");
        }
    }
}