package fr.bbougon.ousontmesaffaires.command.sign;

import fr.bbougon.ousontmesaffaires.command.Command;

public class SignCommand implements Command<String> {
    public SignCommand(final String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    private String data;
}
