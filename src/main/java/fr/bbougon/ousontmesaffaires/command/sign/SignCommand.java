package fr.bbougon.ousontmesaffaires.command.sign;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.infrastructure.security.Signature;

public class SignCommand implements Command<Signature> {
    public SignCommand(final String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    private String data;
}
