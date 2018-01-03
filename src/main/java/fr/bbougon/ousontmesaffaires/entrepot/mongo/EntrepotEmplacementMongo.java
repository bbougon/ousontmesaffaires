package fr.bbougon.ousontmesaffaires.entrepot.mongo;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Emplacement;
import fr.bbougon.ousontmesaffaires.entrepot.*;

public class EntrepotEmplacementMongo extends EntrepotMongo<Emplacement> implements EntrepotEmplacement {

    public EntrepotEmplacementMongo() {
        super(MongoConfiguration.createSession(EntrepotsFichier.configurationBaseDeDonnees().get().getSettings()));
    }

}
