package fr.bbougon.ousontmesaffaires.entrepot.mongo;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Article;
import fr.bbougon.ousontmesaffaires.domaine.emplacement.Emplacement;
import fr.bbougon.ousontmesaffaires.rules.MongoRule;
import fr.bbougon.ousontmesaffaires.test.utils.JsonFileUtils;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ArticleJSON;
import org.junit.*;
import org.mongolink.test.MongolinkRule;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class EntrepotEmplacementMongoTest {

    @Rule
    public MongolinkRule mongolinkRule = MongolinkRule.withPackage("fr.bbougon.ousontmesaffaires.entrepot.mongo.mapping");

    @Rule
    public MongoRule mongoRule = new MongoRule();

    @Test
    public void canPersistEmplacement() {
        Emplacement emplacement = new Emplacement();
        emplacement.add(Article.create(ArticleJSON.from(new JsonFileUtils("json/article.json").getPayload())));
        EntrepotEmplacementMongo entrepotEmplacementMongo = new EntrepotEmplacementMongo(mongoRule.session);

        entrepotEmplacementMongo.persiste(emplacement);
        mongoRule.cleanSession();

        List<Emplacement> emplacements = entrepotEmplacementMongo.getAll();
        assertThat(emplacements).isNotEmpty();
        assertThat(emplacements.get(0).getArticles()).isNotEmpty();
        assertThat(emplacements.get(0).getArticles().get(0).getContent()).isEqualTo("\"{\\\"type\\\":\\\"tshirt\\\",\\\"couleur\\\":\\\"blanc\\\",\\\"taille\\\":\\\"3ans\\\"}\"");
    }

}