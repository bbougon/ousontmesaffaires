package fr.bbougon.ousontmesaffaires.repositories.mongo;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Feature;
import fr.bbougon.ousontmesaffaires.domain.container.Image;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.container.ResizedImage;
import fr.bbougon.ousontmesaffaires.rules.MongoRule;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ContainerName;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import org.junit.Rule;
import org.junit.Test;
import org.mongolink.test.MongolinkRule;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ContainerMongoRepositoryTest {

    @Rule
    public MongolinkRule mongolinkRule = MongolinkRule.withPackage("fr.bbougon.ousontmesaffaires.repositories.mongo.mapping");

    @Rule
    public MongoRule mongoRule = new MongoRule();

    @Test
    public void canPersistContainer() {
        String payload = new FileUtilsForTest("json/t-shirt.json").getContent();
        Container container = Container.create(ContainerName.getNameFromPayload(payload), Item.create(Features.getFeaturesFromPayload(payload)));
        ContainerMongoRepository containerMongoRepository = new ContainerMongoRepository(mongoRule.mongolinkSessionManager);

        containerMongoRepository.persist(container);
        mongoRule.cleanSession();

        List<Container> containers = containerMongoRepository.getAll();
        assertThat(containers).isNotEmpty();
        assertThat(containers.get(0).getName()).isEqualTo("Cave");
        assertThat(containers.get(0).getItems()).isNotEmpty();
        assertThat(containers.get(0).getItems().get(0).getFeatures()).containsAll(Sets.newHashSet(
                Feature.create("type", "tshirt"),
                Feature.create("couleur", "blanc"),
                Feature.create("taille", "3ans")));
    }

    @Test
    public void canAddDescription() {
        String payload = new FileUtilsForTest("json/t-shirt.json").getContent();
        Container container = Container.create(ContainerName.getNameFromPayload(payload), Item.create(Features.getFeaturesFromPayload(payload)));
        container.setDescription("A description");
        ContainerMongoRepository containerMongoRepository = new ContainerMongoRepository(mongoRule.mongolinkSessionManager);

        containerMongoRepository.persist(container);
        mongoRule.cleanSession();

        List<Container> containers = containerMongoRepository.getAll();
        assertThat(containers).isNotEmpty();
        assertThat(containers.get(0).getDescription()).isEqualTo("A description");
    }

    @Test
    public void canAddAnImageToItem() {
        String payload = new FileUtilsForTest("json/t-shirt.json").getContent();
        Container container = Container.create(ContainerName.getNameFromPayload(payload), Item.create(Features.getFeaturesFromPayload(payload)));
        container.getItems().get(0).add(Image.create("abcde", "url", "secure url", Lists.newArrayList(ResizedImage.create("url", "secureUrl", 20d, 10d))));
        ContainerMongoRepository containerMongoRepository = new ContainerMongoRepository(mongoRule.mongolinkSessionManager);

        containerMongoRepository.persist(container);
        mongoRule.cleanSession();

        List<Container> containers = containerMongoRepository.getAll();
        assertThat(containers).isNotEmpty();
        List<Image> images = containers.get(0).getItems().get(0).getImages();
        assertThat(images).hasSize(1);
        assertThat(images.get(0).getUrl()).isEqualTo("url");
        assertThat(images.get(0).getSecureUrl()).isEqualTo("secure url");
        assertThat(images.get(0).getSignature()).isEqualTo("abcde");
        assertThat(images.get(0).getResizedImages()).hasSize(1);
        assertThat(images.get(0).getResizedImages().get(0).getUrl()).isEqualTo("url");
        assertThat(images.get(0).getResizedImages().get(0).getSecureUrl()).isEqualTo("secureUrl");
        assertThat(images.get(0).getResizedImages().get(0).getHeight()).isEqualTo(20d);
        assertThat(images.get(0).getResizedImages().get(0).getWidth()).isEqualTo(10d);
    }
}