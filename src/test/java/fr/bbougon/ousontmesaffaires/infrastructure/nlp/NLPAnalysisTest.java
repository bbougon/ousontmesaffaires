package fr.bbougon.ousontmesaffaires.infrastructure.nlp;

import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NLPAnalysisTest {

    @Test
    public void canCreateFromJsonString() {
        NLPAnalysis nlpAnalysis = NLPAnalysis.fromJsonString(new FileUtilsForTest("json/analyse-response.json").getContent());

        assertThat(nlpAnalysis.categories).hasSize(1);
        assertThat(nlpAnalysis.categories.get(0).hierarchy).isEqualTo("/a/hierrachy");
        assertThat(nlpAnalysis.concepts).hasSize(1);
        assertThat(nlpAnalysis.concepts.get(0).name).isEqualTo("name");
        assertThat(nlpAnalysis.entitiesAnalysis.entities).hasSize(1);
        assertThat(nlpAnalysis.entitiesAnalysis.entities.get(0).name).isEqualTo("name");
        assertThat(nlpAnalysis.entitiesAnalysis.entities.get(0).type).isEqualTo("type");
    }
}