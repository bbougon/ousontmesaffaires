package fr.bbougon.ousontmesaffaires.entrepot;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Emplacement;
import fr.bbougon.ousontmesaffaires.entrepot.mongo.EntrepotsMongos;

public abstract class Entrepots {

    public static void initialise(Entrepots entrepots) {
        SingletonHolder.INSTANCE = entrepots;
    }

    public static Entrepot<Emplacement> emplacement() {
        return SingletonHolder.INSTANCE.getEmplacement();
    }

    protected abstract Entrepot<Emplacement> getEmplacement();

    private static class SingletonHolder {
        static Entrepots INSTANCE = new EntrepotsMongos(MongoConfiguration.startSession(EntrepotsFichier.configurationBaseDeDonnees().get().getSettings()));
    }



}
