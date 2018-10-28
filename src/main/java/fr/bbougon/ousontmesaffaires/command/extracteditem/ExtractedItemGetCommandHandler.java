package fr.bbougon.ousontmesaffaires.command.extracteditem;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.container.FoundExtractedItem;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.apache.commons.lang3.tuple.Pair;

public class ExtractedItemGetCommandHandler implements CommandHandler<ExtractedItemGetCommand, FoundExtractedItem> {

    @Override
    public Pair<FoundExtractedItem, NextEvent> execute(final ExtractedItemGetCommand extractedItemGetCommand) {
        ExtractedItem extractedItem = Repositories.extractedItemRepository().get(extractedItemGetCommand.getExtractedItemUUID())
                .orElse(null);
        if (extractedItem == null) {
            return Pair.of(null, null);
        }
        FoundExtractedItem foundExtractedItem = new FoundExtractedItem(new Codec().urlSafeToBase64(extractedItem.getId().toString()), extractedItem.getItem(),
                new Codec().urlSafeToBase64(extractedItem.getSourceContainer().getId().toString()), extractedItem.getSourceContainer().getName());
        return Pair.of(foundExtractedItem, Nothing.INSTANCE);
    }
}
