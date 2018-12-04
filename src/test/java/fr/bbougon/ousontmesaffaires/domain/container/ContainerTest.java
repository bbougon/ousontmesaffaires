package fr.bbougon.ousontmesaffaires.domain.container;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.image.Image;
import fr.bbougon.ousontmesaffaires.domain.patch.Description;
import fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.test.utils.NLPAnalysisBuilder;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
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
            container.addImageToItem(Image.create("signature", "url", "secure-url", Lists.newArrayList()), "abcd");
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

        List<NLPAnalyzedItem> analyzedItems = container.processItemsNaturalAnalysis(Lists.newArrayList(createAnalysis(item)));

        assertThat(container.getItems().get(0).getFeatures()).hasSize(3);
        assertThat(analyzedItems).hasSize(1);
        assertThat(analyzedItems.get(0).getItem()).isEqualTo(item);
    }

    @Test
    public void canUpdateDescription() {
        Container container = Container.create("Container name", Lists.newArrayList());

        PatchedContainer<Description> newDescription = container.updateDescription("new description");

        assertThat(container.getDescription()).isEqualTo("new description");
        assertThat(newDescription.getPatchedData().getDescription()).isEqualTo("new description");
    }

    @Test
    public void canAddImageToItem() {
        Item item = Item.create("item 2");
        Container container = Container.create("A container", Lists.newArrayList(Item.create("item 1"), item));

        PatchedContainer<Item> patchedContainer = container.addImageToItem(Image.create("signature", "url", "secureUrl", Lists.newArrayList()), item.getItemHash());

        assertThat(patchedContainer.getPatchedData()).isEqualTo(item);
    }

    @Test
    public void canProcessManyItemsNaturalAnalysis() {
        Item item = Item.create("an item");
        Item secondItem = Item.create("another item");
        Container container = Container.create("Container name", Lists.newArrayList(item, secondItem));

        List<NLPAnalyzedItem> analyzedItems = container.processItemsNaturalAnalysis(Lists.newArrayList(createAnalysis(item), createAnalysis(secondItem)));

        Item firstAnalyzedItem = container.getItems().get(0);
        assertThat(firstAnalyzedItem.getFeatures()).hasSize(3);
        Feature feature = new ArrayList<>(firstAnalyzedItem.getFeatures()).get(0);
        assertThat(feature.getFeature()).isEqualTo("a/hierarchy");
        assertThat(feature.getType()).isEqualTo("category");
        Item secondAnalysedItem = container.getItems().get(1);
        assertThat(secondAnalysedItem.getFeatures()).hasSize(3);
        Feature otherFeature = new ArrayList<>(secondAnalysedItem.getFeatures()).get(1);
        assertThat(otherFeature.getFeature()).isEqualTo("name");
        assertThat(otherFeature.getType()).isEqualTo("concept");
        assertThat(analyzedItems).hasSize(2);
    }

    private NLPAnalysis createAnalysis(final Item item) {
        return new NLPAnalysisBuilder()
                    .withDefaultCategories()
                    .withDefaultConcepts()
                    .withDefaultEntitiesAnalysis()
                    .build(item.getItemHash());
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