package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.NLPAnalyzedItem;
import fr.bbougon.ousontmesaffaires.infrastructure.Services;
import fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class NLPCommandHandler implements CommandHandler<NLPCommand, List<NLPAnalyzedItem>> {

    @Override
    public Pair<List<NLPAnalyzedItem>, Event> execute(final NLPCommand nlpCommand) {
        List<NLPAnalysis> nlpAnalyses = Services.nlpService().analyze(nlpCommand.itemsToAnalyse);
        Container container = Repositories.containerRepository().get(nlpCommand.uuid)
                .orElseThrow(() -> new BusinessError("UNEXISTING_CONTAINER"));
        List<NLPAnalyzedItem> analyzedItems = container.processItemsNaturalAnalysis(nlpAnalyses);
        return Pair.of(analyzedItems, Nothing.INSTANCE);
    }
}
