package fr.bbougon.ousontmesaffaires.entrepot.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Article;
import org.mongolink.domain.mapper.ComponentMap;

@SuppressWarnings("UnusedDeclaration")
public class ArticleMapping extends ComponentMap<Article> {

    @Override
    public void map() {
        property().onProperty(Article::getContent);
    }
}
