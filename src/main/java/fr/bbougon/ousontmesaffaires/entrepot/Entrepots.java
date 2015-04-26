package fr.bbougon.ousontmesaffaires.entrepot;

public abstract class Entrepots {

    public static void initialise(Entrepots entrepots) {
        Entrepots.instance = entrepots;
    }

    private static Entrepots instance;
}
