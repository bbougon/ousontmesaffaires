package fr.bbougon.ousontmesaffaires.command.extracteditem;

import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtractedItemGetCommandHandlerTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Test
    public void throwsBusinessErrorOnUnknownExtractedItem() {
        try {
            new ExtractedItemGetCommandHandler().execute(new ExtractedItemGetCommand(new Codec().urlSafeToBase64(UUID.randomUUID().toString())));
        } catch (BusinessError e) {
            assertThat(e.getCode()).isEqualTo("UNKNOWN_ITEM");
        }
    }
}