package fr.bbougon.ousontmesaffaires.infrastructure.bus;

import fr.bbougon.ousontmesaffaires.command.Nothing;
import org.apache.commons.lang3.tuple.Pair;

public class CommandResponse<TResponse> {

    public CommandResponse(final Pair<TResponse, Object> response) {
        this.response = response;
    }

    public TResponse getResponse() {
        return response.getLeft();
    }

    public boolean isEmpty() {
        return response.getLeft() == null && response.getRight() == null || (response.getLeft().equals(Nothing.INSTANCE) && response.getRight() == null);
    }

    private final Pair<TResponse, Object> response;
}
