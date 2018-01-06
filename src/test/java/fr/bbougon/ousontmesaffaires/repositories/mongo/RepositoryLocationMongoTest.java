package fr.bbougon.ousontmesaffaires.repositories.mongo;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.domain.location.*;
import fr.bbougon.ousontmesaffaires.rules.MongoRule;
import fr.bbougon.ousontmesaffaires.test.utils.JsonFileUtils;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ItemJSON;
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
        location.add(Item.create(ItemJSON.from(new JsonFileUtils("json/t-shirt.json").getPayload())));
        LocationMongoRepository locationMongoRepository = new LocationMongoRepository(mongoRule.session);

        locationMongoRepository.persist(location);
        mongoRule.cleanSession();

        List<Location> locations = locationMongoRepository.getAll();
        assertThat(locations).isNotEmpty();
        assertThat(locations.get(0).getItems()).isNotEmpty();
        assertThat(locations.get(0).getItems().get(0).getFeatures()).containsAll(Sets.newHashSet(
                new Feature(new Type("type"), "tshirt"),
                new Feature(new Type("couleur"), "blanc"),
                new Feature(new Type("taille"), "3ans")));
    }

}