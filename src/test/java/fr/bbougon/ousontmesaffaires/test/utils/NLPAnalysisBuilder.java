package fr.bbougon.ousontmesaffaires.test.utils;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis;
import fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis.EntitiesAnalyses;

import java.util.List;

import static fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis.Category;
import static fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis.Concept;
import static fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis.EntitiesAnalyses.Entity;

public class NLPAnalysisBuilder {

    public NLPAnalysisBuilder withDefaultEntitiesAnalysis() {
        return withEntityAnalysis("name", "type");
    }

    public NLPAnalysisBuilder withEntityAnalysis(final String name, final String type) {
        Entity entity = new Entity();
        entity.name = name;
        entity.type = type;
        entitiesAnalyses.entities.add(entity);
        return this;
    }

    public NLPAnalysisBuilder withDefaultCategories() {
        return withCategory("a/hierarchy");
    }

    public NLPAnalysisBuilder withCategory(final String categoryValue) {
        Category category = new Category();
        category.hierarchy = categoryValue;
        categories.add(category);
        return this;
    }

    public NLPAnalysisBuilder withDefaultConcepts() {
        return withConcept("name");
    }

    public NLPAnalysisBuilder withConcept(final String conceptValue) {
        Concept concept = new Concept();
        concept.name = conceptValue;
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
