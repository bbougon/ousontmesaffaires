package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.container.NLPAnalyzedItem;

import java.util.List;
import java.util.UUID;

public class NLPCommand implements Command<List<NLPAnalyzedItem>> {
    public NLPCommand(final List<Item> itemsToAnalyse, final UUID uuid) {
        this.itemsToAnalyse = itemsToAnalyse;
        this.uuid = uuid;
    }

    public List<Item> itemsToAnalyse;
    public final UUID uuid;
}
