package fr.bbougon.ousontmesaffaires.domain.container;

import fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis;
import fr.bbougon.ousontmesaffaires.test.utils.NLPAnalysisBuilder;
import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {

    @Test
    public void canProcessAnalysis() {
        Item item = Item.create("terme1 terme2 terme3");
        NLPAnalysis nlpAnalysis = new NLPAnalysisBuilder()
                .withEntityAnalysis("terme1", "type1")
                .withEntityAnalysis("terme2", "type2")
                .withEntityAnalysis("terme3", "type3")
                .withConcept("concept1")
                .withConcept("concept2")
                .withCategory("hierarchy/1")
                .withCategory("hierarchy/2")
                .build("hash");

        item.processAnalysis(nlpAnalysis);

        assertThat(item.getFeatures()).hasSize(7);
        ArrayList<Feature> features = new ArrayList<>(item.getFeatures());
        assertThat(features).contains(
                Feature.create("category", "hierarchy/1"),
                Feature.create("category", "hierarchy/2"),
                Feature.create("concept", "concept1"),
                Feature.create("concept", "concept2"),
                Feature.create("type1", "terme1"),
                Feature.create("type2", "terme2"),
                Feature.create("type3", "terme3")
        );
    }

}