package fr.bbougon.ousontmesaffaires.command;

public final class Nothing extends NextEvent<Void> {

    private Nothing() {
    }

    public static final Nothing INSTANCE = new Nothing();
}
