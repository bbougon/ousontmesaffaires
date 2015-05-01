package fr.bbougon.ousontmesaffaires.entrepot;

import fr.bbougon.ousontmesaffaires.domaine.Emplacement;

public abstract class Entrepots {

    public static void initialise(Entrepots entrepots) {
        Entrepots.instance = entrepots;
    }

    public static Entrepot<Emplacement> emplacement() {
        return instance.getEmplacement();
    }

    protected abstract Entrepot<Emplacement> getEmplacement();


    private static Entrepots instance;

}
