package fr.bbougon.ousontmesaffaires.infrastructure;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis;

import java.util.List;

public class FakeServices extends Services {

    public void nlpServiceReturns(final List<NLPAnalysis> nlpAnalysis) {
        this.nlpAnalysis.addAll(nlpAnalysis);
    }

    @Override
    protected NLPService getNlpService() {
        return fakeNLPService;
    }

    private class FakeNLPService implements NLPService {
        @Override
        public List<NLPAnalysis> analyze(final List<Item> itemsToAnalyse) {
            return nlpAnalysis;
        }
    }

    private final FakeNLPService fakeNLPService = new FakeNLPService();
    private List<NLPAnalysis> nlpAnalysis = Lists.newArrayList();
}
