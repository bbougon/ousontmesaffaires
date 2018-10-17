package fr.bbougon.ousontmesaffaires.command.extracteditem;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.NextEvent;
import fr.bbougon.ousontmesaffaires.command.Nothing;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

public class ExtractedItemGetCommandHandler implements CommandHandler<ExtractedItemGetCommand, String> {

    @Override
    public Pair<String, NextEvent> execute(final ExtractedItemGetCommand extractedItemGetCommand) {
        ExtractedItem extractedItem = Repositories.extractedItemRepository().get(extractedItemGetCommand.getExtractedItemUUID())
                .orElse(null);
        if (extractedItem == null) {
            return Pair.of(null, null);
        }
        return Pair.of(new GsonBuilder().create()
                .toJson(JsonMappers.fromExtractedItem().map(extractedItem)), Nothing.INSTANCE);
    }
}
