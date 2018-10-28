package fr.bbougon.ousontmesaffaires.command.container;

import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ContainerGetCommandHandlerTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Test
    public void throwsBusinessError() {
        try {
            new ContainerGetCommandHandler().execute(new ContainerGetCommand(new Codec().toBase64(UUID.randomUUID().toString().getBytes())));
            failBecauseExceptionWasNotThrown(BusinessError.class);
        } catch (BusinessError e) {
            assertThat(e.getCode()).isEqualTo("UNEXISTING_CONTAINER");
        }
    }
}