package fr.bbougon.ousontmesaffaires.infrastructure;

import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis;

import java.util.List;

public interface NLPService {
    List<NLPAnalysis> analyze(List<Item> itemsToAnalyse);
}
