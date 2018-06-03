package fr.bbougon.ousontmesaffaires.repositories.mongo;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Feature;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.rules.MongoRule;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ContainerName;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import org.junit.Rule;
import org.junit.Test;
import org.mongolink.test.MongolinkRule;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtractedItemMongoRepositoryTest {

    @Rule
    public MongolinkRule mongolinkRule = MongolinkRule.withPackage("fr.bbougon.ousontmesaffaires.repositories.mongo.mapping");

    @Rule
    public MongoRule mongoRule = new MongoRule();

    @Test
    public void canPersistExtractedItem() {
        String payload = new FileUtilsForTest("json/t-shirt.json").getContent();
        Container container = Container.create(ContainerName.getNameFromPayload(payload), Item.create(Features.getFeaturesFromPayload(payload)));
        Item item = container.getItems().get(0);
        ExtractedItem extractedItem = ExtractedItem.create(item, container);
        ExtractedItemMongoRepository extractedItemRepository = new ExtractedItemMongoRepository(mongoRule.mongolinkSessionManager);

        extractedItemRepository.persist(extractedItem);
        mongoRule.cleanSession();

        List<ExtractedItem> extractedItems = extractedItemRepository.getAll();
        assertThat(extractedItems).isNotEmpty();
        assertThat(extractedItems.get(0).getItem()).isEqualTo(item);
        assertThat(extractedItems.get(0).getSourceContainer().getId()).isEqualTo(container.getId());
        assertThat(extractedItems.get(0).getItem().getFeatures()).containsAll(Sets.newHashSet(
                Feature.create("type", "tshirt"),
                Feature.create("couleur", "blanc"),
                Feature.create("taille", "3ans")));
    }

}