package fr.bbougon.ousontmesaffaires.infrastructure.nlp;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import java.util.List;

public class NLPAnalysis {

    public NLPAnalysis(final String itemHash) {
        this.itemHash = itemHash;
    }

    public static NLPAnalysis fromJsonString(final String value) {
        return new Gson().fromJson(value, NLPAnalysis.class);
    }

    public static class EntitiesAnalysis {
        public static class Entity {
            public String name;
            public String type;

        }
        public List<Entity> entities = Lists.newArrayList();
    }

    public static class Category {
        public String hierarchy;
    }

    public static class Concept {
        public String name;
    }

    public EntitiesAnalysis entitiesAnalysis;
    public List<Category> categories = Lists.newArrayList();
    public List<Concept> concepts = Lists.newArrayList();
    public String itemHash;
}
