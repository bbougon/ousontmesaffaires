package fr.bbougon.ousontmesaffaires.command;

import com.google.common.collect.Sets;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandMiddlewaresTest {

    @Test
    public void unexistingMiddlewarePassThrough() {
        CommandMiddlewares commandMiddlewares = new CommandMiddlewares(Sets.newHashSet());

        assertThat(commandMiddlewares.get(UnknowCommand.class)).isEmpty();
    }

    private class UnknowCommand implements Command{
    }
}