package fr.bbougon.ousontmesaffaires.domaine.emplacement;

import com.google.common.collect.*;

import java.util.List;
import java.util.UUID;

public class Location {

    public Location() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public ImmutableList<Article> getArticles() {
        return ImmutableList.<Article>builder().addAll(articles).build();
    }

    public void add(final Article article) {
        articles.add(article);
    }

    private final UUID id;
    private List<Article> articles = Lists.newArrayList();
}
