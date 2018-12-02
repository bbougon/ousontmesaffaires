package fr.bbougon.ousontmesaffaires.infrastructure.nlp;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.NLPService;

import java.util.List;

public class RemoteNLPService implements NLPService {
    @Override
    public List<NLPAnalysis> analyze(final List<Item> itemsToAnalyse) {
        return Lists.newArrayList();
    }
}
