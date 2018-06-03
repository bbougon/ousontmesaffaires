package fr.bbougon.ousontmesaffaires.infrastructure.security;

public class Signature {

    public Signature(final String signature, final String apiKey) {
        this.signature = signature;
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSignature() {
        return signature;
    }

    private final String signature;
    private final String apiKey;
}
