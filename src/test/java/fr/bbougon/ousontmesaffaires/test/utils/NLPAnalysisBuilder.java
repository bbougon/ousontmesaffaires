package fr.bbougon.ousontmesaffaires.test.utils;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis;
import fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis.EntitiesAnalyses;

import java.util.List;

import static fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis.*;
import static fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis.EntitiesAnalyses.*;

public class NLPAnalysisBuilder {

    public NLPAnalysisBuilder withDefaultEntitiesAnalysis() {
        Entity entity = new Entity();
        entity.name = "name";
        entity.type = "type";
        entitiesAnalyses.entities.add(entity);
        return this;
    }

    public NLPAnalysisBuilder withDefaultCategories() {
        Category category = new Category();
        category.hierarchy = "a/hierarchy";
        categories.add(category);
        return this;
    }

    public NLPAnalysisBuilder withDefaultConcepts() {
        Concept concept = new Concept();
        concept.name = "name";
        concepts.add(concept);
        return this;
    }

    public NLPAnalysis build(final String itemHash) {
        NLPAnalysis nlpAnalysis = new NLPAnalysis(itemHash);
        nlpAnalysis.entitiesAnalyses = entitiesAnalyses;
        nlpAnalysis.categories = categories;
        nlpAnalysis.concepts = concepts;
        return nlpAnalysis;
    }

    private final EntitiesAnalyses entitiesAnalyses = new EntitiesAnalyses();
    private List<Category> categories = Lists.newArrayList();
    private List<Concept> concepts = Lists.newArrayList();
}
