package fr.bbougon.ousontmesaffaires.domaine.emplacement;

import fr.bbougon.ousontmesaffaires.web.ressources.json.ItemJSON;

public class Item {

    @SuppressWarnings("UnusedDeclaration")
    public Item() {
    }

    private Item(final String content) {
        this.content = content;
    }

    public static Item create(final ItemJSON article) {
        return new Item(article.getPayload());
    }

    public String getContent() {
        return content;
    }

    private String content;
}
