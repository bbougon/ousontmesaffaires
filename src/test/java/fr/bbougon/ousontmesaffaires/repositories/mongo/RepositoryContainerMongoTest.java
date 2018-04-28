package fr.bbougon.ousontmesaffaires.repositories.mongo;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Feature;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.rules.MongoRule;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ContainerName;
import org.junit.Rule;
import org.junit.Test;
import org.mongolink.test.MongolinkRule;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RepositoryContainerMongoTest {

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
}