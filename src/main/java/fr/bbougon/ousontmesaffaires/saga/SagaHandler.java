package fr.bbougon.ousontmesaffaires.saga;

import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;

public interface SagaHandler<TSaga extends Saga<TResponse>, TResponse> {

    TResponse run(CommandBus bus, TSaga saga);

}
