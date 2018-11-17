package fr.bbougon.ousontmesaffaires.saga;

import fr.bbougon.ousontmesaffaires.command.Command;

public interface Saga<TResponse> extends Command<TResponse> {
}
