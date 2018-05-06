package fr.bbougon.ousontmesaffaires.infrastructure.security;

public class SecuritySettings {

    public SecuritySettings(final String thirdPartServiceSecret) {
        this.thirdPartServiceSecret = thirdPartServiceSecret;
    }

    public String thirdPartServiceSecret() {
        return thirdPartServiceSecret;
    }

    private final String thirdPartServiceSecret;
}
