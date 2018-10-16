package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtractedItemJsonMapperTest {

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Test
    public void canMapExtractedItems() {
        ExtractedItemJsonMapper mapper = new ExtractedItemJsonMapper();
        Container firstContainer = Container.create("name", Lists.newArrayList(Item.create("value1")));
        ExtractedItem firstExtractedItem = createExtractedItem("value1", firstContainer);
        Container seconContainer = Container.create("name", Lists.newArrayList(Item.create("value1")));
        ExtractedItem secondExtractedItem = createExtractedItem("value2", seconContainer);

        JsonElement extractedItemJson = mapper.map(Lists.newArrayList(firstExtractedItem, secondExtractedItem)
        );

        Codec codec = new Codec();
        String expectedResult = new FileUtilsForTest("json/extractedItem.json").getContent()
                .replace("extracted_item_id_1", codec.urlSafeToBase64(firstExtractedItem.getId().toString()))
                .replace("extracted_item_folder_name_1", firstExtractedItem.getItem().getImageStore().getFolder())
                .replace("extracted_item_hash_1", SecurityService.sha1().cypher(new ItemStringFormatter(firstExtractedItem.getItem()).format().getBytes()))
                .replace("container_id_1", codec.urlSafeToBase64(firstContainer.getId().toString()))
                .replace("container_folder_name_1", firstContainer.getItems().get(0).getImageStore().getFolder())
                .replace("container_hash_1", SecurityService.sha1().cypher(new ItemStringFormatter(firstContainer.getItems().get(0)).format().getBytes()))
                .replace("extracted_item_id_2", codec.urlSafeToBase64(secondExtractedItem.getId().toString()))
                .replace("extracted_item_folder_name_2", secondExtractedItem.getItem().getImageStore().getFolder())
                .replace("extracted_item_hash_2", SecurityService.sha1().cypher(new ItemStringFormatter(secondExtractedItem.getItem()).format().getBytes()))
                .replace("container_id_2", codec.urlSafeToBase64(seconContainer.getId().toString()))
                .replace("container_folder_name_2", seconContainer.getItems().get(0).getImageStore().getFolder())
                .replace("container_hash_2", SecurityService.sha1().cypher(new ItemStringFormatter(seconContainer.getItems().get(0)).format().getBytes()));
        assertThat(new GsonBuilder().setPrettyPrinting()
                .create()
                .toJson(extractedItemJson)).isEqualTo(expectedResult);
    }

    private ExtractedItem createExtractedItem(final String value, final Container container) {
        return ExtractedItem.create(Item.create(value), container);
    }
}