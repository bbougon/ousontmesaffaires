package fr.bbougon.ousontmesaffaires.saga;

import com.google.common.collect.Sets;
import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.command.Event;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SagaMiddlewareTest {

    @Test
    public void canIntercept() {
        SagaMiddleware sagaMiddleware = new SagaMiddleware(Sets.newHashSet(new FakeSagaHandler()));
        FakeCommandBus bus = new FakeCommandBus();

        Pair<String, Event> intercept = sagaMiddleware.intercept(bus, new FakeSaga(), () -> Pair.of("Success", new Event<String>() {}));

        assertThat(intercept).isNotNull();
        assertThat(intercept.getLeft()).isEqualTo("Success");
        assertThat(bus.hasBeenCalled).isTrue();
    }

    private class FakeCommandBus implements CommandBus{

        @Override
        public <TResponse> CommandResponse send(final Command<TResponse> command) {
            hasBeenCalled = true;
            return new CommandResponse(Pair.of("Success", null));
        }

        public boolean hasBeenCalled;
    }

    private class FakeSaga implements Saga<String> {
    }

    private class FakeSagaHandler implements SagaHandler<FakeSaga, String>{

        @Override
        public String run(final CommandBus bus, final FakeSaga saga) {
            CommandResponse send = bus.send(saga);
            return String.valueOf(send.getResponse());
        }
    }
}