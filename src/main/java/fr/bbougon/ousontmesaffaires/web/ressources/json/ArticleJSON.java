package fr.bbougon.ousontmesaffaires.web.ressources.json;

import com.google.gson.*;

public class ArticleJSON {

    private ArticleJSON(final String article) {
        this.article = article;
    }

    private String article;

    public static ArticleJSON from(final String payload) {
        JsonElement parse = new JsonParser().parse(payload);
        JsonObject article = ((JsonObject) parse).getAsJsonObject("article");
        return new ArticleJSON(article.toString());
    }

    public String getPayload() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(article);
    }

}
