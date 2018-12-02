package fr.bbougon.ousontmesaffaires.domain.container;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.image.Image;
import fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.test.utils.NLPAnalysisBuilder;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ContainerTest {

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Test
    public void handleExceptionWhenItemCannotBeFoundOnPatchImage() {
        try {
            Container container = Container.create("a container", Lists.newArrayList());
            container.patchImage(Image.create("signature", "url", "secure-url", Lists.newArrayList()), "abcd");
            failBecauseExceptionWasNotThrown(BusinessError.class);
        } catch (BusinessError e) {
            assertThat(e.getCode()).isEqualTo("UNKNOWN_ITEM_TO_PATCH");
            assertThat(e.getTarget().get()).isEqualTo("a container");
        }
    }

    @Test
    public void handleUnexistingItem() {
        try {
            Container container = Container.create("Container name", Lists.newArrayList(Item.create("an item")));
            Container existingContainer = Container.create("Existing container", Lists.newArrayList(Item.create("existing Item")));
            container.moveItemTo("unexisting-hash", existingContainer);
            failBecauseExceptionWasNotThrown(BusinessError.class);
        } catch (BusinessError e) {
            assertThat(e.getCode()).isEqualTo("UNKNOWN_ITEM_TO_MOVE");
            assertThat(e.getTarget().get()).isEqualTo("Existing container");
        }
    }

    @Test
    public void canProcessOneItemNaturalAnalysis() {
        Item item = Item.create("an item");
        Container container = Container.create("Container name", Lists.newArrayList(item));
        NLPAnalysis nlpAnalysis = new NLPAnalysisBuilder()
                .withDefaultCategories()
                .withDefaultConcepts()
                .withDefaultEntitiesAnalysis()
                .build(item.getItemHash());

        List<NLPAnalyzedItem> analyzedItems = container.processItemsNaturalAnalysis(Lists.newArrayList(nlpAnalysis));

        assertThat(container.getItems().get(0).getFeatures()).hasSize(3);
        assertThat(analyzedItems).hasSize(1);
        assertThat(analyzedItems.get(0).getItem()).isEqualTo(item);
    }

    @Test
    public void handleUnexistingItemWhenProcessingNLPAnalysis() {
        try {
            Container container = Container.create("Container name", Lists.newArrayList());
            NLPAnalysis nlpAnalysis = new NLPAnalysisBuilder().build("unknown-hash");
            container.processItemsNaturalAnalysis(Lists.newArrayList(nlpAnalysis));
            failBecauseExceptionWasNotThrown(BusinessError.class);
        } catch (BusinessError e) {
            assertThat(e.getCode()).isEqualTo("UNKNOWN_ITEM_TO_ANALYSE");
            assertThat(e.getTarget().get()).isEqualTo("Container name");
        }
    }
}