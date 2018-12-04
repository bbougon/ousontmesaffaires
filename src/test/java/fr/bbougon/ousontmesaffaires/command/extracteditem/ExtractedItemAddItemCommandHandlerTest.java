package fr.bbougon.ousontmesaffaires.command.extracteditem;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.infrastructure.security.Sha1Encryptor;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ExtractedItemAddItemCommandHandlerTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Before
    public void before() {
        container = Container.create("Container name", Lists.newArrayList(Item.create("value")));
        container.addItem(Item.create("value2"));
        Repositories.containerRepository().persist(container);
    }

    @Test
    public void canExtractItemFromContainer() {
        ExtractedItemAddItemCommandHandler extractedItemAddItemCommandHandler = new ExtractedItemAddItemCommandHandler();
        Item item = container.getItems().get(1);
        String itemHash = new Sha1Encryptor().cypher(new ItemStringFormatter(item).format().getBytes());

        extractedItemAddItemCommandHandler.execute(new ExtractedItemAddItemCommand(
                "{\"containerId\":\"" + new Codec().urlSafeToBase64(container.getId().toString()) + "\",\"itemHash\":\"" + itemHash + "\"}"));

        List<ExtractedItem> extractedItems = Repositories.extractedItemRepository().getAll();
        assertThat(extractedItems).hasSize(1);
        assertThat(extractedItems.get(0).getItem()).isEqualTo(item);
        assertThat(container.getItems()).doesNotContain(item);
    }

    @Test
    public void handleUnknownItem() {
        try {
            ExtractedItemAddItemCommandHandler extractedItemAddItemCommandHandler = new ExtractedItemAddItemCommandHandler();
            extractedItemAddItemCommandHandler.execute(
                    new ExtractedItemAddItemCommand("{\"containerId\":\"" + new Codec().urlSafeToBase64(container.getId().toString()) + "\",\"itemHash\":\"unknown hash\"}"));
            failBecauseExceptionWasNotThrown(BusinessError.class);
        } catch (BusinessError e){
            assertThat(e.getCode()).isEqualTo("UNKNOWN_ITEM");
            assertThat(e.getTarget().get()).isEqualTo("Container name");
        }
    }

    @Test
    public void handleUnknownContainer() {
        try {
            ExtractedItemAddItemCommandHandler extractedItemAddItemCommandHandler = new ExtractedItemAddItemCommandHandler();
            extractedItemAddItemCommandHandler.execute(
                    new ExtractedItemAddItemCommand("{\"containerId\":\"" + new Codec().urlSafeToBase64(UUID.randomUUID().toString()) + "\",\"itemHash\":\"unknown hash\"}"));
            failBecauseExceptionWasNotThrown(BusinessError.class);
        } catch (BusinessError e){
            assertThat(e.getCode()).isEqualTo("UNEXISTING_CONTAINER");
        }
    }

    private Container container;
}