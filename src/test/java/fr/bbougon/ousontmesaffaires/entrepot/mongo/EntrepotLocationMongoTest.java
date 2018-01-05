package fr.bbougon.ousontmesaffaires.entrepot.mongo;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Article;
import fr.bbougon.ousontmesaffaires.domaine.emplacement.Location;
import fr.bbougon.ousontmesaffaires.rules.MongoRule;
import fr.bbougon.ousontmesaffaires.test.utils.JsonFileUtils;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ArticleJSON;
import org.junit.*;
import org.mongolink.test.MongolinkRule;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class EntrepotLocationMongoTest {

    @Rule
    public MongolinkRule mongolinkRule = MongolinkRule.withPackage("fr.bbougon.ousontmesaffaires.entrepot.mongo.mapping");

    @Rule
    public MongoRule mongoRule = new MongoRule();

    @Test
    public void canPersistLocation() {
        Location location = new Location();
        location.add(Article.create(ArticleJSON.from(new JsonFileUtils("json/article.json").getPayload())));
        LocationRepositoryMongo locationMongoRepository = new LocationRepositoryMongo(mongoRule.session);

        locationMongoRepository.persiste(location);
        mongoRule.cleanSession();

        List<Location> locations = locationMongoRepository.getAll();
        assertThat(locations).isNotEmpty();
        assertThat(locations.get(0).getArticles()).isNotEmpty();
        assertThat(locations.get(0).getArticles().get(0).getContent()).isEqualTo("\"{\\\"type\\\":\\\"tshirt\\\",\\\"couleur\\\":\\\"blanc\\\",\\\"taille\\\":\\\"3ans\\\"}\"");
    }

}