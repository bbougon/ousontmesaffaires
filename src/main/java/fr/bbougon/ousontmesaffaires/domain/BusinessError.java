package fr.bbougon.ousontmesaffaires.domain;

import java.util.Optional;

public class BusinessError extends RuntimeException {

    public BusinessError(final String code, final String target) {
        super(code);
        this.code = code;
        this.target = target;
    }

    public BusinessError(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Optional<String> getTarget() {
        return Optional.ofNullable(target);
    }

    private String code;
    private String target;
}
