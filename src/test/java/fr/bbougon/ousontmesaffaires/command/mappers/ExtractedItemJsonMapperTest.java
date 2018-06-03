package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Feature;
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
        ExtractedItem firstExtractedItem = createExtractedItem("type1", "value1");
        ExtractedItem secondExtractedItem = createExtractedItem("type2", "value2");

        JsonElement extractedItemJson = mapper.map(Lists.newArrayList(firstExtractedItem, secondExtractedItem)
        );

        String expectedResult = new FileUtilsForTest("json/extractedItem.json").getContent()
                .replace("id_1", new Codec().urlSafeToBase64(firstExtractedItem.getId().toString()))
                .replace("folder_name_1", firstExtractedItem.getItem().getImageStore().getFolder())
                .replace("hash_1", SecurityService.sha1().encrypt(new ItemStringFormatter(firstExtractedItem.getItem()).format().getBytes()))
                .replace("id_2", new Codec().urlSafeToBase64(secondExtractedItem.getId().toString()))
                .replace("folder_name_2", secondExtractedItem.getItem().getImageStore().getFolder())
                .replace("hash_2", SecurityService.sha1().encrypt(new ItemStringFormatter(secondExtractedItem.getItem()).format().getBytes()));
        assertThat(new GsonBuilder().setPrettyPrinting()
                .create()
                .toJson(extractedItemJson)).isEqualTo(expectedResult);
    }

    private ExtractedItem createExtractedItem(final String type, final String value) {
        return ExtractedItem.create(
                Item.create(Lists.newArrayList(Feature.create(type, value))),
                Container.create("name", Item.create(Lists.newArrayList(Feature.create("type1", "value1")))));
    }
}