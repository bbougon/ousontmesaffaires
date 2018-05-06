package fr.bbougon.ousontmesaffaires.infrastructure.security;

public class SecuritySettings {

    public SecuritySettings(final String thirdPartServiceSecret, final String apiKey) {
        this.thirdPartServiceSecret = thirdPartServiceSecret;
        this.apiKey = apiKey;
    }

    public String thirdPartServiceSecret() {
        return thirdPartServiceSecret;
    }

    public String apyKey() {
        return apiKey;
    }

    private final String thirdPartServiceSecret;
    private final String apiKey;
}
