package fr.bbougon.ousontmesaffaires.domaine.emplacement;

import fr.bbougon.ousontmesaffaires.web.ressources.json.ArticleJSON;

public class Article {

    @SuppressWarnings("UnusedDeclaration")
    public Article() {
    }

    private Article(final String content) {
        this.content = content;
    }

    public static Article create(final ArticleJSON article) {
        return new Article(article.getPayload());
    }

    public String getContent() {
        return content;
    }

    private String content;
}
