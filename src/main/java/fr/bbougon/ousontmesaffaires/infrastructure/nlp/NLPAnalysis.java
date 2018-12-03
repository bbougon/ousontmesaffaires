package fr.bbougon.ousontmesaffaires.infrastructure.nlp;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis.EntitiesAnalyses.Entity;

import java.util.List;

public class NLPAnalysis {
    public NLPAnalysis(final String itemHash) {
        this.itemHash = itemHash;
    }

    public NLPAnalysis() {

    }

    public static NLPAnalysis fromString(final String value) {
        NLPAnalysis nlpAnalysis = new NLPAnalysis();
        nlpAnalysis.categories.add(new Category());
        nlpAnalysis.concepts.add(new Concept());
        nlpAnalysis.entitiesAnalyses = new EntitiesAnalyses();
        nlpAnalysis.entitiesAnalyses.entities.add(new Entity());
        return nlpAnalysis;
    }

    public static class EntitiesAnalyses {
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

    public EntitiesAnalyses entitiesAnalyses;
    public List<Category> categories = Lists.newArrayList();
    public List<Concept> concepts = Lists.newArrayList();
    public String itemHash;
}
