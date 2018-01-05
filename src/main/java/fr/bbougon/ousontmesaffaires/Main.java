package fr.bbougon.ousontmesaffaires;

public class Main {

    public static void main(String[] args) throws Exception {
        EmbeddedServer.start(Configuration.getServerConfiguration());
    }
}
