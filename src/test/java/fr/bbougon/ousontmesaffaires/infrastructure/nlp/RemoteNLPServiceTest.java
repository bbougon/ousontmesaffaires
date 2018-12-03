package fr.bbougon.ousontmesaffaires.infrastructure.nlp;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.TechnicalError;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.rules.WithFileRepositories;
import fr.bbougon.ousontmesaffaires.test.utils.jaxrs.CustomResteasyCLientBuilder;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class RemoteNLPServiceTest {

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Rule
    public WithFileRepositories withFileRepositories = new WithFileRepositories();

    @Test
    public void canAnalyseOneItemFromRemoteService() {
        Item item = Item.create("an item");
        RemoteNLPService remoteNLPService = new RemoteNLPService(new CustomResteasyCLientBuilder().getResteasyClientBuilder());

        List<NLPAnalysis> nlpAnalysis = remoteNLPService.analyze(Lists.newArrayList(item));

        assertThat(nlpAnalysis).isNotEmpty();
        assertThat(nlpAnalysis).hasSize(1);
        assertAnalysis(item, nlpAnalysis.get(0));
    }

    @Test
    public void canAnalyseManyItemsFromRemoteService() {
        Item item = Item.create("an item");
        Item secondItem = Item.create("another item");
        RemoteNLPService remoteNLPService = new RemoteNLPService(new CustomResteasyCLientBuilder().getResteasyClientBuilder());

        List<NLPAnalysis> nlpAnalysis = remoteNLPService.analyze(Lists.newArrayList(item, secondItem));

        assertThat(nlpAnalysis).hasSize(2);
        assertAnalysis(item, nlpAnalysis.get(0));
        assertAnalysis(secondItem, nlpAnalysis.get(1));
    }

    @Test
    public void handleExceptions() {
        try {
            Item item = Item.create("an item");
            RemoteNLPService remoteNLPService = new RemoteNLPService(new CustomResteasyCLientBuilder().withExceptionOnResponseCall().getResteasyClientBuilder());
            remoteNLPService.analyze(Lists.newArrayList(item));
            failBecauseExceptionWasNotThrown(TechnicalError.class);
        } catch (TechnicalError e) {
            assertThat(e.getCode()).isEqualTo("REMOTE_SERVICE_ERROR");
        }
    }

    private void assertAnalysis(final Item item, final NLPAnalysis nlpAnalysis) {
        assertThat(nlpAnalysis.itemHash).isEqualTo(item.getItemHash());
        assertThat(nlpAnalysis.categories).isNotEmpty();
        assertThat(nlpAnalysis.concepts).isNotEmpty();
        assertThat(nlpAnalysis.entitiesAnalyses.entities).isNotEmpty();
    }

}