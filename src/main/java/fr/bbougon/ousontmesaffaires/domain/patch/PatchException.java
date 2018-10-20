package fr.bbougon.ousontmesaffaires.domain.patch;

import fr.bbougon.ousontmesaffaires.domain.BusinessError;

public class PatchException extends BusinessError {
    public PatchException(final String message) {
        super(message);
    }
}
