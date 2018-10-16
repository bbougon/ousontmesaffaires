package fr.bbougon.ousontmesaffaires.repositories.mongo;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.rules.MongoRule;
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
        Container container = Container.create("Cave", Lists.newArrayList(Item.create("tshirt blanc 3ans")));
        Item item = container.getItems().get(0);
        ExtractedItem extractedItem = ExtractedItem.create(item, container);
        ExtractedItemMongoRepository extractedItemRepository = new ExtractedItemMongoRepository(mongoRule.mongolinkSessionManager);

        extractedItemRepository.persist(extractedItem);
        mongoRule.cleanSession();

        List<ExtractedItem> extractedItems = extractedItemRepository.getAll();
        assertThat(extractedItems).isNotEmpty();
        assertThat(extractedItems.get(0).getItem()).isEqualTo(item);
        assertThat(extractedItems.get(0).getSourceContainer().getId()).isEqualTo(container.getId());
        assertThat(extractedItems.get(0).getItem().getItem()).isEqualTo("tshirt blanc 3ans");
    }

}