package fr.bbougon.ousontmesaffaires.command;

import org.apache.commons.lang3.tuple.Pair;

public interface CommandHandler<TCommand extends Command<TResponse>, TResponse> {

    Pair<TResponse, Object> execute(TCommand tCommand);

}
