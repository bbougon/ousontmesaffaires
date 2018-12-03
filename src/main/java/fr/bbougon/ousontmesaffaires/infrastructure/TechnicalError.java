package fr.bbougon.ousontmesaffaires.infrastructure;

public class TechnicalError extends RuntimeException {
    public TechnicalError(final String code) {
        super(code);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    private String code;
}
