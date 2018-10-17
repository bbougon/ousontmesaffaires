package fr.bbougon.ousontmesaffaires.domain;

public class BusinessError extends RuntimeException {
    public BusinessError(final String code) {
        super(code);
    }
}
