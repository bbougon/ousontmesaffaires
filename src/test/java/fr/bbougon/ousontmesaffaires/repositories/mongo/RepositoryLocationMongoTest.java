package fr.bbougon.ousontmesaffaires.repositories.mongo;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.domain.location.*;
import fr.bbougon.ousontmesaffaires.rules.MongoRule;
import fr.bbougon.ousontmesaffaires.test.utils.JsonFileUtils;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import org.junit.Rule;
import org.junit.Test;
import org.mongolink.test.MongolinkRule;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class RepositoryLocationMongoTest {

    @Rule
    public MongolinkRule mongolinkRule = MongolinkRule.withPackage("fr.bbougon.ousontmesaffaires.repositories.mongo.mapping");

    @Rule
    public MongoRule mongoRule = new MongoRule();

    @Test
    public void canPersistLocation() {
        Location location = new Location();
        location.add(Item.create(Features.getFeaturesFromPayload(new JsonFileUtils("json/t-shirt.json").getPayload())));
        LocationMongoRepository locationMongoRepository = new LocationMongoRepository(mongoRule.session);

        locationMongoRepository.persist(location);
        mongoRule.cleanSession();

        List<Location> locations = locationMongoRepository.getAll();
        assertThat(locations).isNotEmpty();
        assertThat(locations.get(0).getItems()).isNotEmpty();
        assertThat(locations.get(0).getItems().get(0).getFeatures()).containsAll(Sets.newHashSet(
                Feature.create(Type.create("type"), "tshirt"),
                Feature.create(Type.create("couleur"), "blanc"),
                Feature.create(Type.create("taille"), "3ans")));
    }

}