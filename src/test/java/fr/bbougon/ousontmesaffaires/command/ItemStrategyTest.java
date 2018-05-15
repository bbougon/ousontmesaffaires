package fr.bbougon.ousontmesaffaires.command;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Feature;
import fr.bbougon.ousontmesaffaires.domain.container.image.Image;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.container.image.ResizedImage;
import fr.bbougon.ousontmesaffaires.infrastructure.security.Sha1Encryptor;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemStrategyTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Before
    public void before() {
        container = Container.create("Container name",
                Item.create(Lists.newArrayList(Feature.create("type", "value"))));
        Repositories.containerRepository().persist(container);
    }

    @Test
    public void canAddOneImage() {
        String itemHash = new Sha1Encryptor().encrypt(new ItemStringFormatter(container.getItems().get(0)).format().getBytes());
        String jsonPatch = new FileUtilsForTest("json/itemPatch.json").getContent().replace("HASH_TO_REPLACE", itemHash);
        Patch patch = new Gson().fromJson(jsonPatch, Patch.class);

        new ItemStrategy().apply(patch, () -> container);

        List<Image> images = container.getItems().get(0).getImages();
        assertThat(images).hasSize(1);
        Image image = images.get(0);
        assertImage(image, "signature", "image url", "image secure url", 1);
        assertResizedImage(image.getResizedImages().get(0), "url", "secure_url", 200d, 100d);
    }

    @Test
    public void canAddTwoResizedImages() {
        String itemHash = new Sha1Encryptor().encrypt(new ItemStringFormatter(container.getItems().get(0)).format().getBytes());
        String jsonPatch = new FileUtilsForTest("json/itemPatchWithTwoResizedImages.json").getContent().replace("HASH_TO_REPLACE", itemHash);
        Patch patch = new Gson().fromJson(jsonPatch, Patch.class);

        new ItemStrategy().apply(patch, () -> container);

        List<Image> images = container.getItems().get(0).getImages();
        assertThat(images).hasSize(1);
        Image image = images.get(0);
        assertResizedImage(image.getResizedImages().get(1), "url2", "secure_url2", new BigDecimal("400.0").doubleValue(), new BigDecimal("200.0").doubleValue());
    }

    @Test
    public void addImageToExpectedItem() {
        container.add(Item.create(Lists.newArrayList(Feature.create("type2", "value2"))));
        String itemHash = new Sha1Encryptor().encrypt(new ItemStringFormatter(container.getItems().get(1)).format().getBytes());
        String jsonPatch = new FileUtilsForTest("json/itemPatch.json").getContent().replace("HASH_TO_REPLACE", itemHash);
        Patch patch = new Gson().fromJson(jsonPatch, Patch.class);

        new ItemStrategy().apply(patch, () -> container);

        assertThat(container.getItems().get(0).getImages()).isEmpty();
        assertThat(container.getItems().get(1).getImages()).hasSize(1);
    }


    private void assertResizedImage(final ResizedImage resizedImage, final String expectedUrl, final String expectedSecureUrl, final double expectedHeight, final double expectedWidth) {
        assertThat(resizedImage.getUrl()).isEqualTo(expectedUrl);
        assertThat(resizedImage.getSecureUrl()).isEqualTo(expectedSecureUrl);
        assertThat(resizedImage.getHeight()).isEqualTo(expectedHeight);
        assertThat(resizedImage.getWidth()).isEqualTo(expectedWidth);
    }

    private void assertImage(final Image image, final String expectedSignature, final String expectedUrl, final String expectedSecureUrl, final int expectedResizedImageSize) {
        assertThat(image.getSignature()).isEqualTo(expectedSignature);
        assertThat(image.getUrl()).isEqualTo(expectedUrl);
        assertThat(image.getSecureUrl()).isEqualTo(expectedSecureUrl);
        assertThat(image.getResizedImages()).hasSize(expectedResizedImageSize);
    }

    private Container container;
}