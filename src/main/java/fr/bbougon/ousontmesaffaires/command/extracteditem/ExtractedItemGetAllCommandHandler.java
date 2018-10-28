package fr.bbougon.ousontmesaffaires.command.extracteditem;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.container.FoundExtractedItem;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class ExtractedItemGetAllCommandHandler implements CommandHandler<ExtractedItemGetAllCommand, List<FoundExtractedItem>> {

    @Override
    public Pair<List<FoundExtractedItem>, NextEvent> execute(final ExtractedItemGetAllCommand extractedItemGetAllCommand) {
        List<ExtractedItem> extractedItems = Repositories.extractedItemRepository().getAll();
        List<FoundExtractedItem> foundExtractedItems = extractedItems.stream()
                .map(extractedItem -> new FoundExtractedItem(new Codec().urlSafeToBase64(extractedItem.getId().toString()), extractedItem.getItem(),
                        new Codec().urlSafeToBase64(extractedItem.getSourceContainer().getId().toString()), extractedItem.getSourceContainer().getName()))
                .collect(Collectors.toList());
        return Pair.of(foundExtractedItems, Nothing.INSTANCE);
    }
}
