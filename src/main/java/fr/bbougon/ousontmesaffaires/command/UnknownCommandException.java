package fr.bbougon.ousontmesaffaires.command;

class UnknownCommandException extends RuntimeException {
    public UnknownCommandException(final String message) {
        super(message);
    }
}
