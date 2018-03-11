package fr.bbougon.ousontmesaffaires.repositories.mongo;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.domain.location.Feature;
import fr.bbougon.ousontmesaffaires.domain.location.Item;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.rules.MongoRule;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import fr.bbougon.ousontmesaffaires.web.ressources.json.LocationName;
import org.junit.Rule;
import org.junit.Test;
import org.mongolink.test.MongolinkRule;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RepositoryLocationMongoTest {

    @Rule
    public MongolinkRule mongolinkRule = MongolinkRule.withPackage("fr.bbougon.ousontmesaffaires.repositories.mongo.mapping");

    @Rule
    public MongoRule mongoRule = new MongoRule();

    @Test
    public void canPersistLocation() {
        String payload = new FileUtilsForTest("json/t-shirt.json").getContent();
        Location location = Location.create(LocationName.getNameFromPayload(payload), Item.create(Features.getFeaturesFromPayload(payload)));
        LocationMongoRepository locationMongoRepository = new LocationMongoRepository(mongoRule.mongolinkSessionManager);

        locationMongoRepository.persist(location);
        mongoRule.cleanSession();

        List<Location> locations = locationMongoRepository.getAll();
        assertThat(locations).isNotEmpty();
        assertThat(locations.get(0).getLocation()).isEqualTo("Cave");
        assertThat(locations.get(0).getItems()).isNotEmpty();
        assertThat(locations.get(0).getItems().get(0).getFeatures()).containsAll(Sets.newHashSet(
                Feature.create("type", "tshirt"),
                Feature.create("couleur", "blanc"),
                Feature.create("taille", "3ans")));
    }

}