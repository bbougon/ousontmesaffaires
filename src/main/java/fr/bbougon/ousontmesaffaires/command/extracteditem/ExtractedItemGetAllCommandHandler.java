package fr.bbougon.ousontmesaffaires.command.extracteditem;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ExtractedItemGetAllCommandHandler implements CommandHandler<ExtractedItemGetAllCommand, String> {

    @Override
    public Pair<String, Object> execute(final ExtractedItemGetAllCommand extractedItemGetAllCommand) {
        List<ExtractedItem> extractedItems = Repositories.extractedItemRepository().getAll();
        return Pair.of(
                new GsonBuilder().create().toJson(
                        JsonMappers
                                .fromExtractedItem()
                                .map(extractedItems)),
                extractedItems);
    }
}
